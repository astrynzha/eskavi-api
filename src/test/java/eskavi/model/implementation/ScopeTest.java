package eskavi.model.implementation;

import eskavi.model.implementation.moduleimp.Endpoint;
import eskavi.model.implementation.moduleimp.Serializer;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class ScopeTest {
  private Scope scopeA;
  private Scope scopeB;
  private Implementation implementationA;
  private Implementation implementationB;
  private User userA;
  private User userB;
  private ProtocolType protocolType;
  private MessageType messageType;

  @BeforeEach
  void setUp() {
    userA = new User("a@gmail.com", "dfjask;fj",
            UserLevel.PUBLISHING_USER, SecurityQuestion.MAIDEN_NAME, "Julia");
    userB = new User("b@gmail.com", "dfjask;fj",
            UserLevel.PUBLISHING_USER, SecurityQuestion.MAIDEN_NAME, "Julia");
    protocolType = new ProtocolType(0, userA, "protocolType_0", ImplementationScope.SHARED);
    messageType = new MessageType(3, userA, "messageType_3", ImplementationScope.SHARED);
    implementationA = new Endpoint(1, userA, "endpoint_1", ImplementationScope.SHARED, protocolType);
    scopeA = implementationA.getScope();
    implementationB = new Serializer(2, userA, "serializer_2",
            ImplementationScope.SHARED, messageType, protocolType);
    scopeB = implementationB.getScope();
  }

  @Test
  void subscribe() {
    try {
      implementationA.subscribe(userB);
    } catch (IllegalAccessException e) {
      fail(e.getMessage());
    }
    assertTrue(implementationA.isSubscribed(userB), "Could not subscribe a new user");
    assertTrue(implementationA.isSubscribed(userA), "Author is not subscribed");
  }

  @Test
  void unsubscribe() {
    try {
      implementationA.subscribe(userB);
    } catch (IllegalAccessException e) {
      fail(e.getMessage());
    }
    try {
      implementationA.unsubscribe(userB);
    } catch (IllegalAccessException e) {
      fail(e.getMessage());
    }
    assertFalse(implementationA.isSubscribed(userB), "user is still subscribed after unsubscribe");
  }

  @Test
  void setImpScope() {
    try {
      implementationA.subscribe(userB);
    } catch (IllegalAccessException e) {
      fail(e.getMessage());
    }
    assertTrue(implementationA.isSubscribed(userB), "Could not subscribe a new user");
    assertTrue(implementationA.isSubscribed(userA), "Author is not subscribed");
    scopeA.setImpScope(ImplementationScope.PUBLIC);
    assertFalse(implementationA.isSubscribed(userB), "userB is still subscribed after changing the scope");
    assertFalse(implementationA.isSubscribed(userA), "userA is still subscribed after changing the scope");
    scopeA.setImpScope(ImplementationScope.SHARED);
    assertTrue(implementationA.isSubscribed(userA), "Author is not subscribed");
    assertFalse(implementationA.isSubscribed(userB), "user is still subscribed after changing the scope");
    scopeA.setImpScope(ImplementationScope.PRIVATE);
    assertFalse(implementationA.isSubscribed(userB), "userB is still subscribed after changing the scope");
    assertFalse(implementationA.isSubscribed(userA), "userA is still subscribed after changing the scope");
  }
}