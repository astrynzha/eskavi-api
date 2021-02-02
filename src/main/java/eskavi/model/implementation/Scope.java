package eskavi.model.implementation;

import eskavi.model.user.User;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.util.Collection;
import java.util.HashSet;

@Embeddable
public class Scope {
    private ImplementationScope impScope;
    private Collection<User> grantedUsers;
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
        if (impScope == ImplementationScope.SHARED) {
            try {
                implementation.subscribe((User) implementation.getAuthor());
            } catch (IllegalAccessException e) {
              throw new IllegalStateException("This should never happen, scope is now SHARED and " +
                      "the moduleImp can be subscribed to the author", e);
            }
        }
    }

    public boolean isSubscribed(User user) {
        return grantedUsers.contains(user);
    }
}
