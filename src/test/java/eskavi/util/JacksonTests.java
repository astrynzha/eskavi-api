package eskavi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eskavi.model.configuration.*;
import eskavi.model.implementation.*;
import eskavi.model.implementation.moduleimp.*;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;


public class JacksonTests {
    private Configuration configuration;
    private ConfigurationAggregate mapping;
    private TextField port;
    private ImplementationSelect serializerSelect;
    private ImplementationSelect dispatcherSelect;
    private ImplementationSelect deserializerSelect;
    private ImplementationSelect handlerSelect;
    private TextField dummy;
    private ProtocolType protocolTypeA;
    private MessageType messageTypeA;
    private Deserializer deserializer;
    private Serializer serializer;
    private Dispatcher dispatcher;
    private Handler handler;
    private User userA;
    private Endpoint endpoint;


    @BeforeEach
    void setUp() throws IOException {
        dummy = new TextField("dummy", false, new KeyExpression("<dummy>", "<dummy>"), DataType.TEXT);
        dummy.setValue("dummy");
        userA = new User("a@gmail.com", "dfjask;fj",
                UserLevel.PUBLISHING_USER, SecurityQuestion.MAIDEN_NAME, "Julia");
        protocolTypeA = new ProtocolType(0, userA, "protocolType_0", ImplementationScope.SHARED);
        messageTypeA = new MessageType(3, userA, "messageType_3", ImplementationScope.SHARED);

        deserializer = new Deserializer(7, userA, "deserializer_7",
                ImplementationScope.SHARED, dummy, messageTypeA, protocolTypeA);
        serializer = new Serializer(8, userA,
                "serializer_8", ImplementationScope.SHARED, dummy, messageTypeA, protocolTypeA);
        dispatcher = new Dispatcher(9, userA, "dispatcher_9", ImplementationScope.SHARED, dummy, messageTypeA);
        handler = new Handler(10, userA, "handler_10", ImplementationScope.SHARED, dummy, messageTypeA);


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
                new LinkedList<Configuration>(Arrays.asList(dummy, serializerSelect, deserializerSelect, dispatcherSelect)), true);
        configuration = new ConfigurationAggregate("parent", false, new KeyExpression("<parent>", "<parent>"),
                new LinkedList<Configuration>(Arrays.asList(mapping, port)), false);
        endpoint = new Endpoint(11, userA, "endpoint", ImplementationScope.SHARED, configuration, protocolTypeA);
    }

    @Test
    void testConfiguration() throws IOException {
        String result = new ObjectMapper().writeValueAsString(configuration);
        System.out.println(result);
        Configuration copy = new ObjectMapper().readValue(result, Configuration.class);
        System.out.println(copy.toString());
        /*
        ConfigurationAggregate copy = new ObjectMapper().readValue(result, ConfigurationAggregate.class);
        assertEquals(configuration, copy);
        */
    }

    @Test
    void testImplementation() throws JsonProcessingException {
        String result = new ObjectMapper().writeValueAsString(deserializer);
        System.out.println(result);
        //result = "{\"author\":\"a@gmail.com\",\"name\":\"deserializer_7\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"},\"protocolType\":{\"implementationId\":0,\"author\":\"a@gmail.com\",\"name\":\"protocolType_0\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"messageType\":{\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}}}\n";
        Deserializer copy = new ObjectMapper().readValue(result, Deserializer.class);
        System.out.println(copy);
    }

    @Test
    void testImpTypesToJson() throws JsonProcessingException {
        String result = new ObjectMapper().writeValueAsString(ImpType.values());
        System.out.println(result);
    }

    @Test
    void testEndpoint() throws JsonProcessingException {
        String result = new ObjectMapper().writeValueAsString(endpoint);
        System.out.println(result);
    }

    @Test
    void testGeneric() throws JsonProcessingException {
        String result = new ObjectMapper().writeValueAsString(protocolTypeA);
        System.out.println(result);
    }

    @Test
    void testPersistenceManager() throws JsonProcessingException {
        PersistenceManager manager = new PersistenceManager(13, userA, "manager", ImplementationScope.SHARED, dummy);
        String result = new ObjectMapper().writeValueAsString(manager);
        System.out.println(result);
    }
}
