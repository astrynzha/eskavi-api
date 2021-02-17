package eskavi.model.implementation;

import eskavi.model.configuration.Configuration;
import eskavi.model.configuration.DataType;
import eskavi.model.configuration.KeyExpression;
import eskavi.model.configuration.TextField;
import eskavi.model.implementation.moduleimp.*;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModuleImpTest {
    private ProtocolType protocolTypeA;
    private ProtocolType protocolTypeB;
    private MessageType messageTypeA;
    private MessageType messageTypeB;
    private Serializer serializer;
    private Deserializer deserializer;
    private Dispatcher dispatcher;
    private Endpoint endpoint;
    private Handler handler;
    private InteractionStarter interactionStarter;
    private PersistenceManager persistenceManager;
    private AssetConnection assetConnection;
    private List<ImmutableModuleImp> usedImpCollection;
    private Configuration trueConfiguration;

    @BeforeEach
    void setUp() {
        trueConfiguration = new TextField("text", false,
                new KeyExpression("<text>", "<text>"), DataType.TEXT);
        User userA = new User("a@gmail.com", "dfjask;fj",
                UserLevel.PUBLISHING_USER, SecurityQuestion.MAIDEN_NAME, "Julia");
        protocolTypeA = new ProtocolType(0, userA, "protocolType_0", ImplementationScope.SHARED);
        protocolTypeB = new ProtocolType(4, userA, "protocolType_4", ImplementationScope.SHARED);
        messageTypeA = new MessageType(3, userA, "messageType_3", ImplementationScope.SHARED);
        messageTypeB = new MessageType(5, userA, "messageType_5", ImplementationScope.SHARED);
        endpoint = new Endpoint(1, userA, "endpoint_1", ImplementationScope.SHARED, trueConfiguration, protocolTypeA);
        assetConnection = new AssetConnection(6, userA, "assetconnection", ImplementationScope.PUBLIC, trueConfiguration);
        deserializer = new Deserializer(7, userA, "deserializer_7",
                ImplementationScope.SHARED, trueConfiguration, messageTypeA, protocolTypeA);
        serializer = new Serializer(8, userA,
                "serializer_8", ImplementationScope.SHARED, trueConfiguration, messageTypeA, protocolTypeA);
        dispatcher = new Dispatcher(9, userA, "dispatcher_9", ImplementationScope.SHARED, trueConfiguration, messageTypeA);
        handler = new Handler(10, userA, "handler_10", ImplementationScope.SHARED, trueConfiguration, messageTypeA);
        interactionStarter = new InteractionStarter(11, userA,
                "interactionStarter11", ImplementationScope.SHARED, trueConfiguration);
        persistenceManager = new PersistenceManager(12, userA,
                "persistanceManager_12", ImplementationScope.SHARED, trueConfiguration);
        usedImpCollection = new LinkedList<>(Arrays.asList(endpoint, serializer, deserializer,
                dispatcher, handler, assetConnection, interactionStarter, persistenceManager));
    }

    @Test
    void isCompatibleSerializer() {
        Serializer serializer1 = new Serializer(serializer.getId(), (User) serializer.getAuthor(),
                "serializer_copy", serializer.getImplementationScope(), trueConfiguration, serializer.getMessageType(),
                serializer.getProtocolType());
        assertTrue(serializer.isCompatible(usedImpCollection));
        assertFalse(serializer1.isCompatible(usedImpCollection));
        endpoint.setProtocolType(protocolTypeB);
        assertFalse(serializer.isCompatible(usedImpCollection));
        endpoint.setProtocolType(protocolTypeA);
        deserializer.setMessageType(messageTypeB);
        assertFalse(serializer.isCompatible(usedImpCollection));
        deserializer.setMessageType(messageTypeA);
        deserializer.setProtocolType(protocolTypeB);
        assertFalse(serializer.isCompatible(usedImpCollection));
        deserializer.setProtocolType(protocolTypeA);
        dispatcher.setMessageType(messageTypeB);
        assertFalse(serializer.isCompatible(usedImpCollection));
        dispatcher.setMessageType(messageTypeA);
        handler.setMessageType(messageTypeB);
        assertFalse(serializer.isCompatible(usedImpCollection));
        handler.setMessageType(messageTypeA);
    }

    @Test
    void isCompatibleDeserializer() {
        Serializer deserializer1 = new Serializer(deserializer.getId(), (User) deserializer.getAuthor(),
                "deserializer_copy", deserializer.getImplementationScope(), trueConfiguration, deserializer.getMessageType(),
                deserializer.getProtocolType());
        assertTrue(deserializer.isCompatible(usedImpCollection));
        assertFalse(deserializer1.isCompatible(usedImpCollection));
        endpoint.setProtocolType(protocolTypeB);
        assertFalse(deserializer.isCompatible(usedImpCollection));
        endpoint.setProtocolType(protocolTypeA);
        serializer.setMessageType(messageTypeB);
        assertFalse(deserializer.isCompatible(usedImpCollection));
        serializer.setMessageType(messageTypeA);
        serializer.setProtocolType(protocolTypeB);
        assertFalse(deserializer.isCompatible(usedImpCollection));
        serializer.setProtocolType(protocolTypeA);
        dispatcher.setMessageType(messageTypeB);
        assertFalse(deserializer.isCompatible(usedImpCollection));
        dispatcher.setMessageType(messageTypeA);
        handler.setMessageType(messageTypeB);
        assertFalse(deserializer.isCompatible(usedImpCollection));
        handler.setMessageType(messageTypeA);
    }

    @Test
    void isCompatibleEndpoint() {
        Endpoint endpoint1 = new Endpoint(endpoint.getId(), (User) endpoint.getAuthor(),
                "endpoint_copy", endpoint.getImplementationScope(), trueConfiguration, endpoint.getProtocolType());
        assertTrue(endpoint.isCompatible(usedImpCollection));
        assertFalse(endpoint1.isCompatible(usedImpCollection));
        serializer.setMessageType(messageTypeB);
        assertTrue(endpoint.isCompatible(usedImpCollection));
        serializer.setMessageType(messageTypeA);
        serializer.setProtocolType(protocolTypeB);
        assertFalse(endpoint.isCompatible(usedImpCollection));
        serializer.setProtocolType(protocolTypeA);
        deserializer.setMessageType(messageTypeB);
        assertTrue(endpoint.isCompatible(usedImpCollection));
        deserializer.setMessageType(messageTypeA);
        deserializer.setProtocolType(protocolTypeB);
        assertFalse(endpoint.isCompatible(usedImpCollection));
        deserializer.setProtocolType(protocolTypeA);
        dispatcher.setMessageType(messageTypeB);
        assertTrue(endpoint.isCompatible(usedImpCollection));
        dispatcher.setMessageType(messageTypeA);
        handler.setMessageType(messageTypeB);
        assertTrue(endpoint.isCompatible(usedImpCollection));
        handler.setMessageType(messageTypeA);
    }

    @Test
    void isCompatibleDispatcher() {
        Dispatcher dispatcher1 = new Dispatcher(dispatcher.getId(), (User) dispatcher.getAuthor(),
                "dispatcher_copy", dispatcher.getImplementationScope(), trueConfiguration, dispatcher.getMessageType());
        assertTrue(dispatcher.isCompatible(usedImpCollection));
        assertFalse(dispatcher1.isCompatible(usedImpCollection));
        endpoint.setProtocolType(protocolTypeB);
        assertTrue(dispatcher.isCompatible(usedImpCollection));
        endpoint.setProtocolType(protocolTypeA);
        serializer.setMessageType(messageTypeB);
        assertFalse(dispatcher.isCompatible(usedImpCollection));
        serializer.setMessageType(messageTypeA);
        serializer.setProtocolType(protocolTypeB);
        assertTrue(dispatcher.isCompatible(usedImpCollection));
        serializer.setProtocolType(protocolTypeA);
        deserializer.setMessageType(messageTypeB);
        assertFalse(dispatcher.isCompatible(usedImpCollection));
        deserializer.setMessageType(messageTypeA);
        deserializer.setProtocolType(protocolTypeB);
        assertTrue(dispatcher.isCompatible(usedImpCollection));
        deserializer.setProtocolType(protocolTypeA);
        handler.setMessageType(messageTypeB);
        assertFalse(dispatcher.isCompatible(usedImpCollection));
        handler.setMessageType(messageTypeA);
    }

    @Test
    void isCompatibleHandler() {
        Handler handler1 = new Handler(handler.getId(), (User) handler.getAuthor(),
                "handler_copy", handler.getImplementationScope(), trueConfiguration, handler.getMessageType());
        assertTrue(handler.isCompatible(usedImpCollection));
        assertTrue(handler1.isCompatible(usedImpCollection));
        endpoint.setProtocolType(protocolTypeB);
        assertTrue(handler.isCompatible(usedImpCollection));
        endpoint.setProtocolType(protocolTypeA);
        serializer.setMessageType(messageTypeB);
        assertFalse(handler.isCompatible(usedImpCollection));
        serializer.setMessageType(messageTypeA);
        serializer.setProtocolType(protocolTypeB);
        assertTrue(handler.isCompatible(usedImpCollection));
        serializer.setProtocolType(protocolTypeA);
        deserializer.setMessageType(messageTypeB);
        assertFalse(handler.isCompatible(usedImpCollection));
        deserializer.setMessageType(messageTypeA);
        deserializer.setProtocolType(protocolTypeB);
        assertTrue(handler.isCompatible(usedImpCollection));
        deserializer.setProtocolType(protocolTypeA);
        dispatcher.setMessageType(messageTypeB);
        assertFalse(handler.isCompatible(usedImpCollection));
        dispatcher.setMessageType(messageTypeA);
    }

    @Test
    void isCompatibleAssetConnection() {
        AssetConnection assetConnection1 = new AssetConnection(assetConnection.getId(), (User) assetConnection.getAuthor(),
                "AC_copy", assetConnection.getImplementationScope(), trueConfiguration);
        assertTrue(assetConnection.isCompatible(usedImpCollection));
        assertTrue(assetConnection1.isCompatible(usedImpCollection));
        endpoint.setProtocolType(protocolTypeB);
        assertTrue(assetConnection.isCompatible(usedImpCollection));
        endpoint.setProtocolType(protocolTypeA);
        serializer.setMessageType(messageTypeB);
        assertTrue(assetConnection.isCompatible(usedImpCollection));
        serializer.setMessageType(messageTypeA);
        serializer.setProtocolType(protocolTypeB);
        assertTrue(assetConnection.isCompatible(usedImpCollection));
        serializer.setProtocolType(protocolTypeA);
        deserializer.setMessageType(messageTypeB);
        assertTrue(assetConnection.isCompatible(usedImpCollection));
        deserializer.setMessageType(messageTypeA);
        deserializer.setProtocolType(protocolTypeB);
        assertTrue(assetConnection.isCompatible(usedImpCollection));
        deserializer.setProtocolType(protocolTypeA);
        dispatcher.setMessageType(messageTypeB);
        assertTrue(assetConnection.isCompatible(usedImpCollection));
        dispatcher.setMessageType(messageTypeA);
        handler.setMessageType(messageTypeB);
        assertTrue(assetConnection.isCompatible(usedImpCollection));
        handler.setMessageType(messageTypeA);
    }

    @Test
    void isCompatibleInteractionStarter() {
        InteractionStarter interactionStarter1 = new InteractionStarter(interactionStarter.getId(),
                (User) interactionStarter.getAuthor(), "IS_copy", interactionStarter.getImplementationScope(), trueConfiguration);
        assertTrue(interactionStarter.isCompatible(usedImpCollection));
        assertTrue(interactionStarter1.isCompatible(usedImpCollection));
        endpoint.setProtocolType(protocolTypeB);
        assertTrue(interactionStarter.isCompatible(usedImpCollection));
        endpoint.setProtocolType(protocolTypeA);
        serializer.setMessageType(messageTypeB);
        assertTrue(interactionStarter.isCompatible(usedImpCollection));
        serializer.setMessageType(messageTypeA);
        serializer.setProtocolType(protocolTypeB);
        assertTrue(interactionStarter.isCompatible(usedImpCollection));
        serializer.setProtocolType(protocolTypeA);
        deserializer.setMessageType(messageTypeB);
        assertTrue(interactionStarter.isCompatible(usedImpCollection));
        deserializer.setMessageType(messageTypeA);
        deserializer.setProtocolType(protocolTypeB);
        assertTrue(interactionStarter.isCompatible(usedImpCollection));
        deserializer.setProtocolType(protocolTypeA);
        dispatcher.setMessageType(messageTypeB);
        assertTrue(interactionStarter.isCompatible(usedImpCollection));
        dispatcher.setMessageType(messageTypeA);
        handler.setMessageType(messageTypeB);
        assertTrue(interactionStarter.isCompatible(usedImpCollection));
        handler.setMessageType(messageTypeA);
    }

    @Test
    void isCompatiblePersistenceManager() {
        PersistenceManager persistenceManager1 = new PersistenceManager(persistenceManager.getId(),
                (User) persistenceManager.getAuthor(), "PM_copy", persistenceManager.getImplementationScope(), trueConfiguration);
        assertTrue(persistenceManager.isCompatible(usedImpCollection));
        assertFalse(persistenceManager1.isCompatible(usedImpCollection));
        endpoint.setProtocolType(protocolTypeB);
        assertTrue(persistenceManager.isCompatible(usedImpCollection));
        endpoint.setProtocolType(protocolTypeA);
        serializer.setMessageType(messageTypeB);
        assertTrue(persistenceManager.isCompatible(usedImpCollection));
        serializer.setMessageType(messageTypeA);
        serializer.setProtocolType(protocolTypeB);
        assertTrue(persistenceManager.isCompatible(usedImpCollection));
        serializer.setProtocolType(protocolTypeA);
        deserializer.setMessageType(messageTypeB);
        assertTrue(persistenceManager.isCompatible(usedImpCollection));
        deserializer.setMessageType(messageTypeA);
        deserializer.setProtocolType(protocolTypeB);
        assertTrue(persistenceManager.isCompatible(usedImpCollection));
        deserializer.setProtocolType(protocolTypeA);
        dispatcher.setMessageType(messageTypeB);
        assertTrue(persistenceManager.isCompatible(usedImpCollection));
        dispatcher.setMessageType(messageTypeA);
        handler.setMessageType(messageTypeB);
        assertTrue(persistenceManager.isCompatible(usedImpCollection));
        handler.setMessageType(messageTypeA);
    }
}