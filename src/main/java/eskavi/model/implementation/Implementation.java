package eskavi.model.implementation;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eskavi.deserializer.AuthorDeserializer;
import eskavi.model.user.ImmutableUser;
import eskavi.model.user.User;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Implementation implements ImmutableImplementation {
    @Id
    @GeneratedValue
    protected long implementationId;

    @OneToOne
    @JsonIdentityReference(alwaysAsId = true)
    @JsonDeserialize(using = AuthorDeserializer.class)
    private User author;
    private String name;
    @JsonManagedReference
    @Embedded
    private Scope scope;

    public Implementation() {
    }

    protected Implementation(long implementationId, User author, String name, ImplementationScope impScope) {
        this.implementationId = implementationId;
        this.author = author;
        this.name = name;
        this.scope = new Scope(impScope, this);
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

    @Override
    public boolean isSubscribed(ImmutableUser user) {
        if (!(user instanceof User)) { // TODO: mehh instanceof. Is it possible to do without it here?
            return false;
        }
        return scope.isSubscribed((User) user);
    }

    public void setScope(Scope scope) {
        this.scope = scope;
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
                ", author=" + author +
                ", name='" + name + '\'' +
                ", scope=" + scope +
                '}';
    }
}
