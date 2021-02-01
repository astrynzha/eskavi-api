package eskavi.model.implementation;

import eskavi.model.user.User;

import java.util.Collection;
import java.util.HashSet;

public class Scope {
    private ImplementationScope impScope;
    private Collection<User> grantedUsers;
    private final Implementation implementation;

    public Scope(ImplementationScope implementationScope, Implementation implementation) {
        this.impScope = implementationScope;
        this.grantedUsers = new HashSet<>();
        this.implementation = implementation;
    }

    public void subscribe(User user) {
        if (!(impScope == ImplementationScope.SHARED)) {
            System.out.println("Could not subscribe a user to scope. Scope is not SHARED");
            return;
        }
        grantedUsers.add(user);
    }

    public void unsubscribe(User user) {
        if (!(impScope == ImplementationScope.SHARED)) {
            System.out.println("Could not subscribe a user to scope. Scope is not SHARED");
            return;
        }
        if (user == implementation.getAuthor()) {
            System.out.println("Cannot unsubscribe an author from the Implementation");
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
        implementation.subscribe((User) implementation.getAuthor());
    }

    public boolean isSubscribed(User user) {
        return grantedUsers.contains(user);
    }
}
