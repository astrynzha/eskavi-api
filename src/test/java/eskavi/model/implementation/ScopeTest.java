package eskavi.model.implementation;

import eskavi.model.configuration.DataType;
import eskavi.model.configuration.KeyExpression;
import eskavi.model.configuration.TextField;
import eskavi.model.implementation.moduleimp.Endpoint;
import eskavi.model.implementation.moduleimp.Serializer;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScopeTest {
    private Implementation implementationA;
    private Implementation implementationB;
    private User userA;
    private User userB;
    private ProtocolType protocolType;
    private MessageType messageType;

    @BeforeEach
    void setUp() {
        TextField template = new TextField("template", false, new KeyExpression("<template>", "<template>"), DataType.TEXT);
        userA = new User("a@gmail.com", "dfjask;fj",
                UserLevel.PUBLISHING_USER, SecurityQuestion.MAIDEN_NAME, "Julia");
        userB = new User("b@gmail.com", "dfjask;fj",
                UserLevel.PUBLISHING_USER, SecurityQuestion.MAIDEN_NAME, "Julia");
        protocolType = new ProtocolType(0, userA, "protocolType_0", ImplementationScope.SHARED);
        messageType = new MessageType(3, userA, "messageType_3", ImplementationScope.SHARED);
        implementationA = new Endpoint(1, userA, "endpoint_1", ImplementationScope.SHARED, template, protocolType);
        implementationB = new Serializer(2, userA, "serializer_2",
                ImplementationScope.SHARED, template, messageType, protocolType);
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

        implementationA.unsubscribe(userB);

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
        implementationA.setScope(new Scope(ImplementationScope.PUBLIC));
        assertFalse(implementationA.isSubscribed(userB), "userB is still subscribed after changing the scope");
        implementationA.setScope(new Scope(ImplementationScope.SHARED));
        assertFalse(implementationA.isSubscribed(userB), "user is still subscribed after changing the scope");
        implementationA.setScope(new Scope(ImplementationScope.PRIVATE));
        assertFalse(implementationA.isSubscribed(userB), "userB is still subscribed after changing the scope");
    }
}