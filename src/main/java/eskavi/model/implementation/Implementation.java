package eskavi.model.implementation;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eskavi.deserializer.ScopeDeserializer;
import eskavi.deserializer.UserByIdDeserializer;
import eskavi.model.user.ImmutableUser;
import eskavi.model.user.User;

import javax.persistence.*;
import java.util.ArrayList;
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
/*
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "implementationId")

 */
public abstract class Implementation implements ImmutableImplementation {
    @Id
    @GeneratedValue
    protected long implementationId;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    @JsonDeserialize(using = UserByIdDeserializer.class)
    private User author;
    private String name;
    @JsonProperty("scope")
    @JsonDeserialize(using = ScopeDeserializer.class)
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Scope scope;

    public Implementation() {
    }

    /**
     * Create an implementation object.
     * If the scope is shared - author is added to the scope object here.
     *
     * @param implementationId id of this object
     * @param author           author of this object
     * @param name             name of this object
     * @param impScope         implementation scope of this object
     */
    protected Implementation(long implementationId, User author, String name, ImplementationScope impScope) {
        this.implementationId = implementationId;
        setAuthor(author);
        this.name = name;
        this.scope = new Scope(impScope);
    }

    /**
     * Subscribes a user to the scope object of this implementation and makes sure that the implementation
     * is subscribed to the user as well
     *
     * @param user User to subscribe
     * @throws IllegalAccessException if the scope is not SHARED
     */
    public void subscribe(User user) throws IllegalAccessException {
        if (isSubscribed(user) || getAuthor().equals(user)) {
            return;
        }
        scope.subscribe(user);
        user.subscribe(this);
    }

    /**
     * Unsubscribes a user from the scope object of this implementation and makes sure that the implementation
     * is unsubscribed from the user as well
     *
     * @param user User to subscribe
     * @throws IllegalAccessException if the scope is not SHARED
     */
    public void unsubscribe(User user) {
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
                && scope.getScopeId() >= 0 && scope.getGrantedUsers() != null;
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
        return author;
    }

    /**
     * Assign a new scope object to this implementation object.
     * If the scope is shared -> subscribes the author
     *
     * @param scope new scope object
     */
    public void setScope(Scope scope) {
        if (this.scope != null) {
            Collection<User> subscribers = new ArrayList<>(this.getSubscribed());
            for (User user : subscribers) {
                user.unsubscribe(this);
            }
        }
        this.scope = scope;
    }

    /**
     * Returns all users that are subscriped to this Implementation. This does not include the author!
     * @return Collection of all subscribed users
     */
    @JsonIgnore
    @Override
    public Collection<User> getSubscribed() {
        return scope.getGrantedUsers();
    }

    @Override
    public boolean isSubscribed(ImmutableUser user) {
        if (!(user instanceof User)) {
            return false;
        }
        return this.author.equals(user) || scope.isSubscribed((User) user);
    }

    @JsonIgnore
    @Override
    public ImplementationScope getImplementationScope() {
        return scope.getImpScope();
    }

    /**
     * Sets the author to the given User this method should usually never be used outside the constructor
     * @param author
     */
    public void setAuthor(User author) {
        if (this.author != null) {
            this.author.unsubscribe(this);
        }
        this.author = author;
        try {
            author.subscribe(this);
        } catch (IllegalAccessException e) {
            //this should never be reached here
            assert false;
        }
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
