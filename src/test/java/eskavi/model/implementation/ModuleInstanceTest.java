package eskavi.model.implementation;

import eskavi.model.configuration.*;
import eskavi.model.implementation.moduleimp.*;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModuleInstanceTest {
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
    private Configuration configuration;
    private ConfigurationAggregate mapping;
    private TextField port;
    private ImplementationSelect serializerSelect;
    private ImplementationSelect dispatcherSelect;
    private ImplementationSelect deserializerSelect;
    private ImplementationSelect handlerSelect;
    private TextField dummy;
    private ModuleInstance instance;

    @BeforeEach
    void setUp() {
        dummy = new TextField("dummy", false, new KeyExpression("<dummy>", "<dummy>"), DataType.TEXT);
        dummy.setValue("dummy");
        User userA = new User("a@gmail.com", "dfjask;fj",
                UserLevel.PUBLISHING_USER, SecurityQuestion.MAIDEN_NAME, "Julia");
        protocolTypeA = new ProtocolType(0, userA, "protocolType_0", ImplementationScope.SHARED);
        protocolTypeB = new ProtocolType(4, userA, "protocolType_4", ImplementationScope.SHARED);
        messageTypeA = new MessageType(3, userA, "messageType_3", ImplementationScope.SHARED);
        messageTypeB = new MessageType(5, userA, "messageType_5", ImplementationScope.SHARED);
        endpoint = new Endpoint(1, userA, "endpoint_1", ImplementationScope.SHARED, dummy, protocolTypeA);
        assetConnection = new AssetConnection(6, userA, "assetconnection", ImplementationScope.PUBLIC, dummy);
        deserializer = new Deserializer(7, userA, "deserializer_7",
                ImplementationScope.SHARED, dummy, messageTypeA, protocolTypeA);
        serializer = new Serializer(8, userA,
                "serializer_8", ImplementationScope.SHARED, dummy, messageTypeA, protocolTypeA);
        dispatcher = new Dispatcher(9, userA, "dispatcher_9", ImplementationScope.SHARED, dummy, messageTypeA);
        handler = new Handler(10, userA, "handler_10", ImplementationScope.SHARED, dummy, messageTypeA);
        interactionStarter = new InteractionStarter(11, userA,
                "interactionStarter11", ImplementationScope.SHARED, dummy);
        persistenceManager = new PersistenceManager(12, userA,
                "persistanceManager_12", ImplementationScope.SHARED, dummy);
        usedImpCollection = new LinkedList<>(Arrays.asList(endpoint, assetConnection, interactionStarter, persistenceManager));

        this.dummy = new TextField("dummy", false, new KeyExpression("<dummy>", "<dummy>"), DataType.TEXT);
        this.dummy.setValue("dummy");

        port = new TextField("port", false, new KeyExpression("<port>", "<port>"), DataType.NUMBER);
        port.setValue("8080");
        HashSet<ImmutableGenericImp> generics = new HashSet<>();
        generics.add(messageTypeA);
        generics.add(protocolTypeA);
        serializerSelect = new ImplementationSelect("serializer", false, new KeyExpression("<serializer>", "<serializer>"),
                generics, ImpType.SERIALIZER);
        serializerSelect.setInstance(new ModuleInstance(serializer));
        deserializerSelect = new ImplementationSelect("deserializer", false, new KeyExpression("<deserializer>", "<deserializer>"),
                generics, ImpType.DESERIALIZER);
        deserializerSelect.setInstance(new ModuleInstance(deserializer));
        generics = new HashSet<>();
        generics.add(messageTypeA);
        dispatcherSelect = new ImplementationSelect("dispatcher", false, new KeyExpression("<dispatcher>", "<dispatcher>"),
                generics, ImpType.DISPATCHER);
        handlerSelect = new ImplementationSelect("handler", false, new KeyExpression("<handler>", "<handler>"),
                generics, ImpType.HANDLER);
        handlerSelect.setInstance(new ModuleInstance(handler));
        dispatcher.setConfigurationRoot(handlerSelect);
        dispatcherSelect.setInstance(new ModuleInstance(dispatcher));
        mapping = new ConfigurationAggregate("mapping", false, new KeyExpression("<mapping>", "<mapping>"),
                new LinkedList<Configuration>(Arrays.asList(this.dummy, serializerSelect, deserializerSelect, dispatcherSelect)), true);
        configuration = new ConfigurationAggregate("parent", false, new KeyExpression("<parent>", "<parent>"),
                new LinkedList<Configuration>(Arrays.asList(mapping, port)), false);
        endpoint.setConfigurationRoot(configuration);
        instance = new ModuleInstance(endpoint);
    }

    @Test
    void resolveConfiguration() {
        assertEquals("<parent>" +
                "<mapping>" +
                "<dummy>dummy<dummy>" +
                "<serializer><dummy>dummy<dummy><serializer>" +
                "<deserializer><dummy>dummy<dummy><deserializer>" +
                "<dispatcher><handler><dummy>dummy<dummy><handler><dispatcher>" +
                "<mapping>" +
                "<port>8080<port>" +
                "<parent>", instance.resolveConfiguration());
    }

    @Test
    void isCompatible() {
        HashSet<ImmutableModuleImp> others = new HashSet<>();
        others.addAll(usedImpCollection);
        others.addAll(instance.getInstanceConfiguration().getDependentModuleImps());
        assertEquals(true, instance.isCompatible(others));
    }

    @Test
    void isCompatibleFailure() {
        serializer.setMessageType(messageTypeB);
        HashSet<ImmutableModuleImp> others = new HashSet<>();
        others.addAll(usedImpCollection);
        others.addAll(instance.getInstanceConfiguration().getDependentModuleImps());
        assertEquals(false, instance.isCompatible(others));
    }
}