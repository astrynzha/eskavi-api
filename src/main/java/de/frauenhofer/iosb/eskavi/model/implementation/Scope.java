package de.frauenhofer.iosb.eskavi.model.implementation;

import de.frauenhofer.iosb.eskavi.model.user.User;

import java.util.Collection;
import java.util.HashSet;

// TODO: update database upon subscribing
public class Scope {
  private ImplementationScope impScope;
  private Collection<User> grantedUsers;

  public Scope(ImplementationScope implementationScope) {
    this.impScope = implementationScope;
    this.grantedUsers = new HashSet<>();
  }

  public void subscribe(User user) {
    if (!(impScope == ImplementationScope.SHARED)) {
      throw new IllegalStateException("Could not subscribe a user to scope. Scope is not SHARED");
    }
    grantedUsers.add(user);
  }

  public void unsubscribe(User user) {
    if (!(impScope == ImplementationScope.SHARED)) {
      throw new IllegalStateException("Could not subscribe a user to scope. Scope is not SHARED");
    }
    grantedUsers.remove(user);
  }

  // TODO: in front end warn that you will lose all the currently chosen shared users before changing scope from shared
  public void setImpScope(ImplementationScope impScope) {
    this.impScope = impScope;
    grantedUsers = new HashSet<>();
  }

  public ImplementationScope getImpScope() {
    return impScope;
  }

  public boolean isSubscribed(User user) {
    return grantedUsers.contains(user);
  }
}
