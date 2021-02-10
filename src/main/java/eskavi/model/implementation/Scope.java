package eskavi.model.implementation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eskavi.model.user.User;

import javax.persistence.*;
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
    @OneToMany
    private Collection<User> grantedUsers;

    public Scope(ImplementationScope implementationScope) {
        this.impScope = implementationScope;
        this.grantedUsers = new HashSet<>();
    }

    public Scope() {

    }

    public void subscribe(User user) throws IllegalAccessException {
        if (!(impScope.equals(ImplementationScope.SHARED))) {
            throw new IllegalAccessException("Could not subscribe a user to scope. Scope is not SHARED");
        }
        grantedUsers.add(user);
    }

    public void unsubscribe(User user) throws IllegalAccessException {
        if (!(impScope.equals(ImplementationScope.SHARED))) {
            throw new IllegalAccessException("Could not subscribe a user to scope. Scope is not SHARED");
        }
        grantedUsers.remove(user);
    }

    public ImplementationScope getImpScope() {
        return impScope;
    }

    // TODO: in front end warn that you will lose all the currently chosen shared users before changing scope from shared
    public void setImpScope(ImplementationScope impScope) {
        this.impScope = impScope;
        grantedUsers = new HashSet<>();
    }

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
        return impScope == scope.impScope && Objects.equals(grantedUsers, scope.grantedUsers);
    }

    @Override
    public String toString() {
        return "Scope{" +
                "impScope=" + impScope +
                ", grantedUsers=" + grantedUsers + "}";
    }
}
