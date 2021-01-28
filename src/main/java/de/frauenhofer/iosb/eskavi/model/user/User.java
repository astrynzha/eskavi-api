package de.frauenhofer.iosb.eskavi.model.user;

import de.frauenhofer.iosb.eskavi.model.implementation.Scope;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

// TODO: update data base upon subscribing
public class User {
  // TODO: one list of implementations or separate for moduleImps and GenericImps
  private List<Scope> scopeList;

  public User() {
    this.scopeList = new LinkedList<>();
  }

  public void subscribe(Scope scope) {
    if (isSubscribed(scope)) {
      return;
    }
    scopeList.add(scope);
    scope.subscribe(this);
  }

  public void unsubscribe(Scope scope) {
    if (!isSubscribed(scope)) {
      return;
    }
    scopeList.remove(scope);
    scope.unsubscribe(this);
  }

  public boolean isSubscribed(Scope scope) {
    return scopeList.contains(scope);
  }
}
