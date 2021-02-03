package eskavi.model.implementation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import eskavi.model.user.User;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@Embeddable
public class Scope {
    @Enumerated(EnumType.STRING)
    private ImplementationScope impScope;
    @OneToMany
    private Collection<User> grantedUsers;
    @JsonBackReference
    @OneToOne
    private Implementation implementation;

    public Scope(ImplementationScope implementationScope, Implementation implementation) {
        this.impScope = implementationScope;
        this.grantedUsers = new HashSet<>();
        this.implementation = implementation;
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
        /*
        if (impScope == ImplementationScope.SHARED) {
            try {
                implementation.subscribe((User) implementation.getAuthor());
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("This should never happen, scope is now SHARED and " +
                        "the moduleImp can be subscribed to the author", e);
            }
        }*/
    }

    public boolean isSubscribed(User user) {
        return grantedUsers.contains(user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(impScope, grantedUsers, implementation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scope scope = (Scope) o;
        return impScope == scope.impScope && Objects.equals(grantedUsers, scope.grantedUsers) && Objects.equals(implementation, scope.implementation);
    }

    @Override
    public String toString() {
        return "Scope{" +
                "impScope=" + impScope +
                ", grantedUsers=" + grantedUsers +
                ", implementation=" + implementation +
                '}';
    }
}
