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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
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
    @OneToOne(cascade = CascadeType.PERSIST)
    private Scope scope;

    public Implementation() {
    }

    /**
     * Create an implementation object.
     * If the scope is shared - author is added to the scope object here.
     * @param implementationId id of this object
     * @param author author of this object
     * @param name name of this object
     * @param impScope implementation scope of this object
     */
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

    /**
     * Subscribes a user to the scope object of this implementation and makes sure that the implementation
     * is subscribed to the user as well
     * @param user User to subscribe
     * @throws IllegalAccessException if the scope is not SHARED
     */
    public void subscribe(User user) throws IllegalAccessException {
        if (isSubscribed(user)) {
            return;
        }
        scope.subscribe(user);
        user.subscribe(this);
    }

    /**
     * Unsubscribes a user from the scope object of this implementation and makes sure that the implementation
     * is unsubscribed from the user as well
     * @param user User to subscribe
     * @throws IllegalAccessException if the scope is not SHARED
     */
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
    @JsonIgnore
    public boolean isValid() {
        return implementationId >= 0 && author != null && name != null && scope != null && scope.getImpScope() != null
                && scope.getScopeId() >= 0 && scope.getGrantedUsers() != null
                // when the scope is SHARED -> author has to be subscribed, otherwise grantedUsers is empty
                && (((scope.getImpScope().equals(ImplementationScope.SHARED)) && scope.getGrantedUsers().size() == 1
                       && scope.getGrantedUsers().contains(author))
                    || (!scope.getImpScope().equals(ImplementationScope.SHARED) && scope.getGrantedUsers().size() == 0));
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

    /**
     * Assign a new scope object to this implementation object.
     * If the scope is shared -> subscribes the author
     * @param scope new scope object
     */
    public void setScope(Scope scope) {
        this.scope = scope;
        if (scope.getImpScope().equals(ImplementationScope.SHARED)) {
            try {
                scope.subscribe((User) getAuthor());
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("this cannot happen, scope is checked, before adding a new user");
            }
        }
    }

    @JsonIgnore
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

    @JsonIgnore
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
