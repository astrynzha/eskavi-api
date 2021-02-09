package eskavi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eskavi.model.configuration.*;
import eskavi.model.implementation.*;
import eskavi.model.implementation.moduleimp.Deserializer;
import eskavi.model.implementation.moduleimp.Dispatcher;
import eskavi.model.implementation.moduleimp.Handler;
import eskavi.model.implementation.moduleimp.Serializer;
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


    @BeforeEach
    void setUp() throws IOException {
        TextField template = new TextField("template", false, new KeyExpression("<template>", "<template>"), DataType.TEXT);
        userA = new User("a@gmail.com", "dfjask;fj",
                UserLevel.PUBLISHING_USER, SecurityQuestion.MAIDEN_NAME, "Julia");
        protocolTypeA = new ProtocolType(0, userA, "protocolType_0", ImplementationScope.SHARED);
        messageTypeA = new MessageType(3, userA, "messageType_3", ImplementationScope.SHARED);

        deserializer = new Deserializer(7, userA, "deserializer_7",
                ImplementationScope.SHARED, template, messageTypeA, protocolTypeA);
        serializer = new Serializer(8, userA,
                "serializer_8", ImplementationScope.SHARED, template, messageTypeA, protocolTypeA);
        dispatcher = new Dispatcher(9, userA, "dispatcher_9", ImplementationScope.SHARED, template, messageTypeA);
        handler = new Handler(10, userA, "handler_10", ImplementationScope.SHARED, template, messageTypeA);

        dummy = new TextField("dummy", false, new KeyExpression("<dummy>", "<dummy>"), DataType.TEXT);
        dummy.setValue("dummy");
        port = new TextField("port", false, new KeyExpression("<port>", "<port>"), DataType.NUMBER);
        port.setValue("8080");
        HashSet<ImmutableGenericImp> generics = new HashSet<>();
        generics.add(messageTypeA);
        generics.add(protocolTypeA);
        serializerSelect = new ImplementationSelect("serializer", false, new KeyExpression("<serializer>", "<serializer>"),
                generics, ImpType.SERIALIZER);
        serializerSelect.setInstance(new ModuleInstance(serializer, dummy));
        deserializerSelect = new ImplementationSelect("deserializer", false, new KeyExpression("<deserializer>", "<deserializer>"),
                generics, ImpType.DESERIALIZER);
        deserializerSelect.setInstance(new ModuleInstance(deserializer, dummy));
        generics = new HashSet<>();
        generics.add(messageTypeA);
        dispatcherSelect = new ImplementationSelect("dispatcher", false, new KeyExpression("<dispatcher>", "<dispatcher>"),
                generics, ImpType.DISPATCHER);
        handlerSelect = new ImplementationSelect("handler", false, new KeyExpression("<handler>", "<handler>"),
                generics, ImpType.HANDLER);
        handlerSelect.setInstance(new ModuleInstance(handler, dummy));
        dispatcherSelect.setInstance(new ModuleInstance(dispatcher, handlerSelect));
        mapping = new ConfigurationAggregate("mapping", false, new KeyExpression("<mapping>", "<mapping>"),
                new LinkedList<Configuration>(Arrays.asList(dummy, serializerSelect, deserializerSelect, dispatcherSelect)), true);
        configuration = new ConfigurationAggregate("parent", false, new KeyExpression("<parent>", "<parent>"),
                new LinkedList<Configuration>(Arrays.asList(mapping, port)), false);
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
        result = "{\"author\":\"a@gmail.com\",\"name\":\"deserializer_7\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"},\"protocolType\":{\"implementationId\":0,\"author\":\"a@gmail.com\",\"name\":\"protocolType_0\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"messageType\":{\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}}}\n";
        Deserializer copy = new ObjectMapper().readValue(result, Deserializer.class);
        System.out.println(copy);
    }

}
