package eskavi.model.implementation;

import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenericImpTest {
    private ProtocolType protocolTypeA;
    private ProtocolType protocolTypeB;
    private MessageType messageTypeA;
    private MessageType messageTypeB;

    @BeforeEach
    void setUp() {
        User user = new User("a@gmail.com", "dfjask;fj",
                UserLevel.PUBLISHING_USER, SecurityQuestion.MAIDEN_NAME, "Julia");
        protocolTypeA = new ProtocolType(0, user, "protocolType_0", ImplementationScope.SHARED);
        protocolTypeB = new ProtocolType(1, user, "protocolType_1", ImplementationScope.PRIVATE);
        messageTypeA = new MessageType(2, user, "messageType_2", ImplementationScope.SHARED);
        messageTypeB = new MessageType(3, user, "messageType_3", ImplementationScope.PUBLIC);
    }

    @Test
    void checkCompatibility() {
        assertFalse(protocolTypeA.checkCompatibility(protocolTypeB));
        assertFalse(messageTypeA.checkCompatibility(messageTypeB));
        assertTrue(messageTypeA.checkCompatibility(protocolTypeB));
        assertTrue(protocolTypeA.checkCompatibility(messageTypeB));
        assertTrue(messageTypeA.checkCompatibility(messageTypeA));
        assertTrue(protocolTypeA.checkCompatibility(protocolTypeA));
    }
}