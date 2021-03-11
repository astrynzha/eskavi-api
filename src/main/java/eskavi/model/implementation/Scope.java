package eskavi.model.implementation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eskavi.model.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * This class describes if this MI is shared with other users
 * and exactly which user groups have access to this MI.
 *
 * @author Andrii Strynzha, David Kaufmann, Maximilian Georg
 * @version 1.0.0
 */
@Entity
public class Scope {
    @Id
    @GeneratedValue
    protected long scopeId;
    @Enumerated(EnumType.STRING)
    private ImplementationScope impScope;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<User> grantedUsers;

    /**
     * Creates a scope object
     *
     * @param implementationScope PUBLIC, SHARED or PRIVATE
     */
    public Scope(ImplementationScope implementationScope) {
        this.impScope = implementationScope;
        this.grantedUsers = new HashSet<>();
    }

    public Scope(ImplementationScope impScope, Collection<User> grantedUsers) {
        this.impScope = impScope;
        this.grantedUsers = grantedUsers;
    }

    public Scope() {

    }

    /**
     * subscribes a user to the scope
     *
     * @param user user to subscribe
     * @throws IllegalAccessException if the scope is not SHARED
     */
    public void subscribe(User user) throws IllegalAccessException {
        if (!(impScope.equals(ImplementationScope.SHARED))) {
            throw new IllegalAccessException("Could not subscribe a user to scope. Scope is not SHARED");
        }
        grantedUsers.add(user);
    }

    /**
     * unsubscribes a user from the scope
     *
     * @param user user to unsubscribe
     * @throws IllegalAccessException if the scope is not SHARED
     */
    public void unsubscribe(User user) throws IllegalAccessException {
        if (!(impScope.equals(ImplementationScope.SHARED))) {
            throw new IllegalAccessException("Could not subscribe a user to scope. Scope is not SHARED");
        }
        grantedUsers.remove(user);
    }

    public ImplementationScope getImpScope() {
        return impScope;
    }

    /**
     * sets the new Implementation scope and empties the grantedUsers set if the new scope is private or public
     *
     * @param impScope new implementation scope
     */
    // TODO: in front end warn that you will lose all the currently chosen shared users before changing scope from shared
    public void setImpScope(ImplementationScope impScope) {
        this.impScope = impScope;
        grantedUsers = new HashSet<>();
    }

    /**
     * checks if the user is subscribed to this scope, i.e. has access to the MI, to which this scope belongs
     *
     * @param user user to check
     * @return true if subscribed
     */
    public boolean isSubscribed(User user) {
        return grantedUsers.contains(user);
    }

    public long getScopeId() {
        return this.scopeId;
    }

    public void setScopeId(long id) {
        this.scopeId = id;
    }

    public Collection<User> getGrantedUsers() {
        return grantedUsers;
    }

    @Override
    public int hashCode() {
        return Objects.hash(impScope, grantedUsers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scope scope = (Scope) o;
        //TODO really ugly work around because hibernate persistance bag is broken!
        return impScope == scope.impScope && (getGrantedUsers() != null ? new ArrayList(getGrantedUsers()) : new ArrayList())
                .equals(scope.getGrantedUsers() != null ? new ArrayList(scope.getGrantedUsers()) : new ArrayList());
    }

    @Override
    public String toString() {
        return "Scope{" +
                "impScope=" + impScope +
                ", grantedUsers=" + grantedUsers + "}";
    }
}
