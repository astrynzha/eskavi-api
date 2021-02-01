package eskavi.model.implementation;

import eskavi.model.configuration.Configuration;
import eskavi.model.configuration.DataType;
import eskavi.model.configuration.KeyExpression;
import eskavi.model.configuration.TextField;
import eskavi.model.implementation.moduleimp.AssetConnection;
import eskavi.model.implementation.moduleimp.Deserializer;
import eskavi.model.implementation.moduleimp.Dispatcher;
import eskavi.model.implementation.moduleimp.Endpoint;
import eskavi.model.implementation.moduleimp.Handler;
import eskavi.model.implementation.moduleimp.InteractionStarter;
import eskavi.model.implementation.moduleimp.PersistenceManager;
import eskavi.model.implementation.moduleimp.Serializer;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
    endpoint = new Endpoint(1, userA, "endpoint_1", ImplementationScope.SHARED, protocolTypeA);
    assetConnection = new AssetConnection(6, userA, "assetconnection", ImplementationScope.PUBLIC);
    deserializer = new Deserializer(7, userA, "deserializer_7",
            ImplementationScope.SHARED, messageTypeA, protocolTypeA);
    serializer = new Serializer(8, userA,
            "serializer_8", ImplementationScope.SHARED, messageTypeA, protocolTypeA);
    dispatcher = new Dispatcher(9, userA, "dispatcher_9", ImplementationScope.SHARED, messageTypeA);
    handler = new Handler(10, userA, "handler_10", ImplementationScope.SHARED, messageTypeA);
    interactionStarter = new InteractionStarter(11, userA,
            "interactionStarter11", ImplementationScope.SHARED);
    persistenceManager = new PersistenceManager(12, userA,
            "persistanceManager_12", ImplementationScope.SHARED);
    usedImpCollection = new LinkedList<>(Arrays.asList(endpoint, serializer, deserializer,
            dispatcher, handler, assetConnection, interactionStarter, persistenceManager));
  }

  @Test
  void isCompatibleSerializer() {
    Serializer serializer1 = new Serializer(serializer.getImplementationId(), (User) serializer.getAuthor(),
            "serializer_copy", serializer.getScope().getImpScope(), serializer.getMessageType(),
            serializer.getProtocolType());
    assertTrue(serializer.isCompatible(usedImpCollection, trueConfiguration));
    assertFalse(serializer1.isCompatible(usedImpCollection, trueConfiguration));
    endpoint.setProtocolType(protocolTypeB);
    assertFalse(serializer.isCompatible(usedImpCollection, trueConfiguration));
    endpoint.setProtocolType(protocolTypeA);
    deserializer.setMessageType(messageTypeB);
    assertFalse(serializer.isCompatible(usedImpCollection, trueConfiguration));
    deserializer.setMessageType(messageTypeA);
    deserializer.setProtocolType(protocolTypeB);
    assertFalse(serializer.isCompatible(usedImpCollection, trueConfiguration));
    deserializer.setProtocolType(protocolTypeA);
    dispatcher.setMessageType(messageTypeB);
    assertFalse(serializer.isCompatible(usedImpCollection, trueConfiguration));
    dispatcher.setMessageType(messageTypeA);
    handler.setMessageType(messageTypeB);
    assertFalse(serializer.isCompatible(usedImpCollection, trueConfiguration));
    handler.setMessageType(messageTypeA);
  }

  @Test
  void isCompatibleDeserializer() {
    Serializer deserializer1 = new Serializer(deserializer.getImplementationId(), (User) deserializer.getAuthor(),
            "deserializer_copy", deserializer.getScope().getImpScope(), deserializer.getMessageType(),
            deserializer.getProtocolType());
    assertTrue(deserializer.isCompatible(usedImpCollection, trueConfiguration));
    assertFalse(deserializer1.isCompatible(usedImpCollection, trueConfiguration));
    endpoint.setProtocolType(protocolTypeB);
    assertFalse(deserializer.isCompatible(usedImpCollection, trueConfiguration));
    endpoint.setProtocolType(protocolTypeA);
    serializer.setMessageType(messageTypeB);
    assertFalse(deserializer.isCompatible(usedImpCollection, trueConfiguration));
    serializer.setMessageType(messageTypeA);
    serializer.setProtocolType(protocolTypeB);
    assertFalse(deserializer.isCompatible(usedImpCollection, trueConfiguration));
    serializer.setProtocolType(protocolTypeA);
    dispatcher.setMessageType(messageTypeB);
    assertFalse(deserializer.isCompatible(usedImpCollection, trueConfiguration));
    dispatcher.setMessageType(messageTypeA);
    handler.setMessageType(messageTypeB);
    assertFalse(deserializer.isCompatible(usedImpCollection, trueConfiguration));
    handler.setMessageType(messageTypeA);
  }

  @Test
  void isCompatibleEndpoint() {
    Endpoint endpoint1 = new Endpoint(endpoint.getImplementationId(), (User) endpoint.getAuthor(),
            "endpoint_copy", endpoint.getScope().getImpScope(), endpoint.getProtocolType());
    assertTrue(endpoint.isCompatible(usedImpCollection, trueConfiguration));
    assertFalse(endpoint1.isCompatible(usedImpCollection, trueConfiguration));
    serializer.setMessageType(messageTypeB);
    assertTrue(endpoint.isCompatible(usedImpCollection, trueConfiguration));
    serializer.setMessageType(messageTypeA);
    serializer.setProtocolType(protocolTypeB);
    assertFalse(endpoint.isCompatible(usedImpCollection, trueConfiguration));
    serializer.setProtocolType(protocolTypeA);
    deserializer.setMessageType(messageTypeB);
    assertTrue(endpoint.isCompatible(usedImpCollection, trueConfiguration));
    deserializer.setMessageType(messageTypeA);
    deserializer.setProtocolType(protocolTypeB);
    assertFalse(endpoint.isCompatible(usedImpCollection, trueConfiguration));
    deserializer.setProtocolType(protocolTypeA);
    dispatcher.setMessageType(messageTypeB);
    assertTrue(endpoint.isCompatible(usedImpCollection, trueConfiguration));
    dispatcher.setMessageType(messageTypeA);
    handler.setMessageType(messageTypeB);
    assertTrue(endpoint.isCompatible(usedImpCollection, trueConfiguration));
    handler.setMessageType(messageTypeA);
  }

  @Test
  void isCompatibleDispatcher() {
    Dispatcher dispatcher1 = new Dispatcher(dispatcher.getImplementationId(), (User) dispatcher.getAuthor(),
            "dispatcher_copy", dispatcher.getScope().getImpScope(), dispatcher.getMessageType());
    assertTrue(dispatcher.isCompatible(usedImpCollection, trueConfiguration));
    assertFalse(dispatcher1.isCompatible(usedImpCollection, trueConfiguration));
    endpoint.setProtocolType(protocolTypeB);
    assertTrue(dispatcher.isCompatible(usedImpCollection, trueConfiguration));
    endpoint.setProtocolType(protocolTypeA);
    serializer.setMessageType(messageTypeB);
    assertFalse(dispatcher.isCompatible(usedImpCollection, trueConfiguration));
    serializer.setMessageType(messageTypeA);
    serializer.setProtocolType(protocolTypeB);
    assertTrue(dispatcher.isCompatible(usedImpCollection, trueConfiguration));
    serializer.setProtocolType(protocolTypeA);
    deserializer.setMessageType(messageTypeB);
    assertFalse(dispatcher.isCompatible(usedImpCollection, trueConfiguration));
    deserializer.setMessageType(messageTypeA);
    deserializer.setProtocolType(protocolTypeB);
    assertTrue(dispatcher.isCompatible(usedImpCollection, trueConfiguration));
    deserializer.setProtocolType(protocolTypeA);
    handler.setMessageType(messageTypeB);
    assertFalse(dispatcher.isCompatible(usedImpCollection, trueConfiguration));
    handler.setMessageType(messageTypeA);
  }

  @Test
  void isCompatibleHandler() {
    Handler handler1 = new Handler(handler.getImplementationId(), (User) handler.getAuthor(),
            "handler_copy", handler.getScope().getImpScope(), handler.getMessageType());
    assertTrue(handler.isCompatible(usedImpCollection, trueConfiguration));
    assertTrue(handler1.isCompatible(usedImpCollection, trueConfiguration));
    endpoint.setProtocolType(protocolTypeB);
    assertTrue(handler.isCompatible(usedImpCollection, trueConfiguration));
    endpoint.setProtocolType(protocolTypeA);
    serializer.setMessageType(messageTypeB);
    assertFalse(handler.isCompatible(usedImpCollection, trueConfiguration));
    serializer.setMessageType(messageTypeA);
    serializer.setProtocolType(protocolTypeB);
    assertTrue(handler.isCompatible(usedImpCollection, trueConfiguration));
    serializer.setProtocolType(protocolTypeA);
    deserializer.setMessageType(messageTypeB);
    assertFalse(handler.isCompatible(usedImpCollection, trueConfiguration));
    deserializer.setMessageType(messageTypeA);
    deserializer.setProtocolType(protocolTypeB);
    assertTrue(handler.isCompatible(usedImpCollection, trueConfiguration));
    deserializer.setProtocolType(protocolTypeA);
    dispatcher.setMessageType(messageTypeB);
    assertFalse(handler.isCompatible(usedImpCollection, trueConfiguration));
    dispatcher.setMessageType(messageTypeA);
  }

  @Test
  void isCompatibleAssetConnection() {
    AssetConnection assetConnection1 = new AssetConnection(assetConnection.getImplementationId(), (User) assetConnection.getAuthor(),
            "AC_copy", assetConnection.getScope().getImpScope());
    assertTrue(assetConnection.isCompatible(usedImpCollection, trueConfiguration));
    assertTrue(assetConnection1.isCompatible(usedImpCollection, trueConfiguration));
    endpoint.setProtocolType(protocolTypeB);
    assertTrue(assetConnection.isCompatible(usedImpCollection, trueConfiguration));
    endpoint.setProtocolType(protocolTypeA);
    serializer.setMessageType(messageTypeB);
    assertTrue(assetConnection.isCompatible(usedImpCollection, trueConfiguration));
    serializer.setMessageType(messageTypeA);
    serializer.setProtocolType(protocolTypeB);
    assertTrue(assetConnection.isCompatible(usedImpCollection, trueConfiguration));
    serializer.setProtocolType(protocolTypeA);
    deserializer.setMessageType(messageTypeB);
    assertTrue(assetConnection.isCompatible(usedImpCollection, trueConfiguration));
    deserializer.setMessageType(messageTypeA);
    deserializer.setProtocolType(protocolTypeB);
    assertTrue(assetConnection.isCompatible(usedImpCollection, trueConfiguration));
    deserializer.setProtocolType(protocolTypeA);
    dispatcher.setMessageType(messageTypeB);
    assertTrue(assetConnection.isCompatible(usedImpCollection, trueConfiguration));
    dispatcher.setMessageType(messageTypeA);
    handler.setMessageType(messageTypeB);
    assertTrue(assetConnection.isCompatible(usedImpCollection, trueConfiguration));
    handler.setMessageType(messageTypeA);
  }

  @Test
  void isCompatibleInteractionStarter() {
    InteractionStarter interactionStarter1 = new InteractionStarter(interactionStarter.getImplementationId(),
            (User) interactionStarter.getAuthor(), "IS_copy", interactionStarter.getScope().getImpScope());
    assertTrue(interactionStarter.isCompatible(usedImpCollection, trueConfiguration));
    assertTrue(interactionStarter1.isCompatible(usedImpCollection, trueConfiguration));
    endpoint.setProtocolType(protocolTypeB);
    assertTrue(interactionStarter.isCompatible(usedImpCollection, trueConfiguration));
    endpoint.setProtocolType(protocolTypeA);
    serializer.setMessageType(messageTypeB);
    assertTrue(interactionStarter.isCompatible(usedImpCollection, trueConfiguration));
    serializer.setMessageType(messageTypeA);
    serializer.setProtocolType(protocolTypeB);
    assertTrue(interactionStarter.isCompatible(usedImpCollection, trueConfiguration));
    serializer.setProtocolType(protocolTypeA);
    deserializer.setMessageType(messageTypeB);
    assertTrue(interactionStarter.isCompatible(usedImpCollection, trueConfiguration));
    deserializer.setMessageType(messageTypeA);
    deserializer.setProtocolType(protocolTypeB);
    assertTrue(interactionStarter.isCompatible(usedImpCollection, trueConfiguration));
    deserializer.setProtocolType(protocolTypeA);
    dispatcher.setMessageType(messageTypeB);
    assertTrue(interactionStarter.isCompatible(usedImpCollection, trueConfiguration));
    dispatcher.setMessageType(messageTypeA);
    handler.setMessageType(messageTypeB);
    assertTrue(interactionStarter.isCompatible(usedImpCollection, trueConfiguration));
    handler.setMessageType(messageTypeA);
  }

  @Test
  void isCompatiblePersistenceManager() {
    PersistenceManager persistenceManager1 = new PersistenceManager(persistenceManager.getImplementationId(),
            (User) persistenceManager.getAuthor(), "PM_copy", persistenceManager.getScope().getImpScope());
    assertTrue(persistenceManager.isCompatible(usedImpCollection, trueConfiguration));
    assertTrue(persistenceManager1.isCompatible(usedImpCollection, trueConfiguration));
    endpoint.setProtocolType(protocolTypeB);
    assertTrue(persistenceManager.isCompatible(usedImpCollection, trueConfiguration));
    endpoint.setProtocolType(protocolTypeA);
    serializer.setMessageType(messageTypeB);
    assertTrue(persistenceManager.isCompatible(usedImpCollection, trueConfiguration));
    serializer.setMessageType(messageTypeA);
    serializer.setProtocolType(protocolTypeB);
    assertTrue(persistenceManager.isCompatible(usedImpCollection, trueConfiguration));
    serializer.setProtocolType(protocolTypeA);
    deserializer.setMessageType(messageTypeB);
    assertTrue(persistenceManager.isCompatible(usedImpCollection, trueConfiguration));
    deserializer.setMessageType(messageTypeA);
    deserializer.setProtocolType(protocolTypeB);
    assertTrue(persistenceManager.isCompatible(usedImpCollection, trueConfiguration));
    deserializer.setProtocolType(protocolTypeA);
    dispatcher.setMessageType(messageTypeB);
    assertTrue(persistenceManager.isCompatible(usedImpCollection, trueConfiguration));
    dispatcher.setMessageType(messageTypeA);
    handler.setMessageType(messageTypeB);
    assertTrue(persistenceManager.isCompatible(usedImpCollection, trueConfiguration));
    handler.setMessageType(messageTypeA);
  }
}