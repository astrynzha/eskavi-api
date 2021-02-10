package eskavi.model.implementation;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eskavi.deserializer.UserByIdDeserializer;
import eskavi.model.implementation.moduleimp.*;
import eskavi.model.user.ImmutableUser;
import eskavi.model.user.User;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * This abstract class implements the ImmutableImplementation interface and serves as a common
 * representation of any implementation. It abstracts the common features of GenericImp and ModuleImp,
 * making it a central entity of our business logic.
 *
 * @author Andrii Strynzha, David Kaufmann, Maximilian Georg
 * @version 1.0.0
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "implementationId")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AssetConnection.class, name = "AssetConnection"),
        @JsonSubTypes.Type(value = Deserializer.class, name = "Deserializer"),
        @JsonSubTypes.Type(value = Dispatcher.class, name = "Dispatcher"),
        @JsonSubTypes.Type(value = Endpoint.class, name = "Endpoint"),
        @JsonSubTypes.Type(value = Handler.class, name = "Handler"),
        @JsonSubTypes.Type(value = InteractionStarter.class, name = "InteractionStarter"),
        @JsonSubTypes.Type(value = PersistenceManager.class, name = "PersistenceManager"),
        @JsonSubTypes.Type(value = Serializer.class, name = "Serializer")
})
public abstract class Implementation implements ImmutableImplementation {
    @Id
    @GeneratedValue
    protected long implementationId;

    @OneToOne
    @JsonIdentityReference(alwaysAsId = true)
    @JsonDeserialize(using = UserByIdDeserializer.class)
    private User author;
    private String name;
    //@JsonDeserialize(using = ScopeDeserializer.class)
    @OneToOne
    private Scope scope;

    public Implementation() {
    }

    protected Implementation(long implementationId, User author, String name, ImplementationScope impScope) {
        this.implementationId = implementationId;
        this.author = author;
        this.name = name;
        this.scope = new Scope(impScope);
        if (impScope == ImplementationScope.SHARED) {
            try {
                scope.subscribe(author);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("This should never happen, scope is SHARED and " +
                        "the moduleImp can be subscribed to the author", e);
            }
        }
    }

    public void subscribe(User user) throws IllegalAccessException {
        if (isSubscribed(user)) {
            return;
        }
        scope.subscribe(user);
        user.subscribe(this);
    }

    public void unsubscribe(User user) throws IllegalAccessException {
        if (!isSubscribed(user)) {
            return;
        }
        scope.unsubscribe(user);
        user.unsubscribe(this);
    }

    /**
     * checks if the Implementation is correctly constructed
     *
     * @return
     */
    public boolean isValid() {
        return implementationId >= 0 && author != null && name != null && scope != null && scope.getImpScope() != null
                && scope.getScopeId() >= 0 && scope.getGrantedUsers() != null
                && scope.getGrantedUsers().size() == 0;
    }

    @Override
    public long getImplementationId() {
        return implementationId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ImmutableUser getAuthor() {
        return (ImmutableUser) author; // TODO: should the cast be here or in view?
    }

    @Override
    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) throws IllegalAccessException {
        this.scope = scope;
        scope.subscribe((User) getAuthor());
    }

    @Override
    public Collection<ImmutableUser> getUsers() {
        return new HashSet<>(scope.getGrantedUsers());
    }

    @Override
    public boolean isSubscribed(ImmutableUser user) {
        if (!(user instanceof User)) { // TODO: mehh instanceof. Is it possible to do without it here?
            return false;
        }
        return scope.isSubscribed((User) user);
    }

    @Override
    public ImplementationScope getImplementationScope() {
        return scope.getImpScope();
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setImplementationId(long implementationId) {
        this.implementationId = implementationId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(implementationId, author, name, scope);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Implementation that = (Implementation) o;
        return implementationId == that.implementationId && Objects.equals(author, that.author) && Objects.equals(name, that.name) && Objects.equals(scope, that.scope);
    }

    @Override
    public String toString() {
        return "{" +
                "implementationId=" + implementationId +
                ", author=" + author.toString() +
                ", name='" + name + '\'' +
                ", scope=" + scope.toString() +
                '}';
    }
}
