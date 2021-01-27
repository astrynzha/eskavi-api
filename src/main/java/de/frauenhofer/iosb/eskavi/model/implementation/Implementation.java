package de.frauenhofer.iosb.eskavi.model.implementation;

import de.frauenhofer.iosb.eskavi.model.user.ImmutableUser;
import de.frauenhofer.iosb.eskavi.model.user.User;

public abstract class Implementation implements ImmutableImplementation {
  private long implementationId;
  private User author;
  private String name;
  private Scope scope;

  // TODO: where do ID's come from?
  protected Implementation(long implementationId, User author, String name, Scope scope) {
    this.implementationId = implementationId;
    this.author = author;
    this.name = name;
    scope.subscribe(author);
    this.scope = scope;
  }

  public void subscribe(User user) {
    scope.subscribe(user);
  }

  public void unsubscribe(User user) {
    scope.unsubscribe(user);
  }

  public boolean isSubscribed(ImmutableUser user) {
    return scope.isSubscribed((User) user); // TODO: is conversion OK?
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setScope(Scope scope) {
    this.scope = scope;
  }

  public long getImplementationId() {
    return implementationId;
  }

  public ImmutableUser getAuthor() {
    return (ImmutableUser) author;
  }

  public String getName() {
    return name;
  }

  public Scope getScope() {
    return scope;
  }
}
