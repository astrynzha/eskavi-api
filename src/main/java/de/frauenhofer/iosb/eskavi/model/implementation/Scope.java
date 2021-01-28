package de.frauenhofer.iosb.eskavi.model.implementation;

import de.frauenhofer.iosb.eskavi.model.user.User;

import java.util.LinkedList;
import java.util.List;

// TODO: update database upon subscribing
public class Scope {
  private ImplementationScope impScope;
  private List<User> grantedUsers;

  public Scope(ImplementationScope implementationScope) {
    this.impScope = implementationScope;
    this.grantedUsers = new LinkedList<>();
  }

  public void subscribe(User user) {
    if (!(impScope == ImplementationScope.SHARED)) {
      throw new IllegalStateException("Could not subscribe a user to scope. Scope is not SHARED");
    }
    if (isSubscribed(user)) {
      return;
    }
    grantedUsers.add(user);
    user.subscribe(this);
  }

  public void unsubscribe(User user) {
    if (!(impScope == ImplementationScope.SHARED)) {
      throw new IllegalStateException("Could not subscribe a user to scope. Scope is not SHARED");
    }
    if (!isSubscribed(user)) {
      return;
    }
    grantedUsers.remove(user);
    user.unsubscribe(this);
  }

  // TODO: in front end warn that you will lose all the currently chosen shared users before changing scope from shared
  public void setImpScope(ImplementationScope impScope) {
    this.impScope = impScope;
    grantedUsers = new LinkedList<>();
  }

  public ImplementationScope getImpScope() {
    return impScope;
  }

  public boolean isSubscribed(User user) {
    return grantedUsers.contains(user);
  }
}
