package eskavi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eskavi.controller.requests.user.RegisterRequest;
import eskavi.model.configuration.*;
import eskavi.model.implementation.*;
import eskavi.model.implementation.moduleimp.*;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.print.attribute.standard.JobKOctets;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        //System.out.println(result);
        assertEquals("{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"<parent>\",\"expressionEnd\":\"<parent>\"},\"enforceCompatibility\":false,\"name\":\"parent\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"<mapping>\",\"expressionEnd\":\"<mapping>\"},\"enforceCompatibility\":true,\"name\":\"mapping\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<dummy>\",\"expressionEnd\":\"<dummy>\"},\"value\":\"dummy\",\"dataType\":\"TEXT\",\"name\":\"dummy\",\"allowMultiple\":false},{\"jsonTypeInfo\":\"IMPLEMENTATION_SELECT\",\"keyExpression\":{\"expressionStart\":\"<serializer>\",\"expressionEnd\":\"<serializer>\"},\"instance\":{\"moduleImp\":{\"jsonTypeInfo\":\"SERIALIZER\",\"implementationId\":8,\"author\":\"a@gmail.com\",\"name\":\"serializer_8\",\"configurationRoot\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<dummy>\",\"expressionEnd\":\"<dummy>\"},\"value\":\"dummy\",\"dataType\":\"TEXT\",\"name\":\"dummy\",\"allowMultiple\":false}]},\"protocolType\":{\"jsonTypeInfo\":\"PROTOCOL_TYPE\",\"implementationId\":0,\"author\":\"a@gmail.com\",\"name\":\"protocolType_0\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"messageType\":{\"jsonTypeInfo\":\"MESSAGE_TYPE\",\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"instanceConfiguration\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<dummy>\",\"expressionEnd\":\"<dummy>\"},\"value\":\"dummy\",\"dataType\":\"TEXT\",\"name\":\"dummy\",\"allowMultiple\":false}]}},\"generics\":[{\"jsonTypeInfo\":\"MESSAGE_TYPE\",\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},{\"jsonTypeInfo\":\"PROTOCOL_TYPE\",\"implementationId\":0,\"author\":\"a@gmail.com\",\"name\":\"protocolType_0\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}}],\"type\":\"SERIALIZER\",\"name\":\"serializer\",\"allowMultiple\":false},{\"jsonTypeInfo\":\"IMPLEMENTATION_SELECT\",\"keyExpression\":{\"expressionStart\":\"<deserializer>\",\"expressionEnd\":\"<deserializer>\"},\"instance\":{\"moduleImp\":{\"jsonTypeInfo\":\"DESERIALIZER\",\"implementationId\":7,\"author\":\"a@gmail.com\",\"name\":\"deserializer_7\",\"configurationRoot\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<dummy>\",\"expressionEnd\":\"<dummy>\"},\"value\":\"dummy\",\"dataType\":\"TEXT\",\"name\":\"dummy\",\"allowMultiple\":false}]},\"protocolType\":{\"jsonTypeInfo\":\"PROTOCOL_TYPE\",\"implementationId\":0,\"author\":\"a@gmail.com\",\"name\":\"protocolType_0\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"messageType\":{\"jsonTypeInfo\":\"MESSAGE_TYPE\",\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"instanceConfiguration\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<dummy>\",\"expressionEnd\":\"<dummy>\"},\"value\":\"dummy\",\"dataType\":\"TEXT\",\"name\":\"dummy\",\"allowMultiple\":false}]}},\"generics\":[{\"jsonTypeInfo\":\"MESSAGE_TYPE\",\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},{\"jsonTypeInfo\":\"PROTOCOL_TYPE\",\"implementationId\":0,\"author\":\"a@gmail.com\",\"name\":\"protocolType_0\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}}],\"type\":\"DESERIALIZER\",\"name\":\"deserializer\",\"allowMultiple\":false},{\"jsonTypeInfo\":\"IMPLEMENTATION_SELECT\",\"keyExpression\":{\"expressionStart\":\"<dispatcher>\",\"expressionEnd\":\"<dispatcher>\"},\"instance\":{\"moduleImp\":{\"jsonTypeInfo\":\"DISPATCHER\",\"implementationId\":9,\"author\":\"a@gmail.com\",\"name\":\"dispatcher_9\",\"configurationRoot\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"IMPLEMENTATION_SELECT\",\"keyExpression\":{\"expressionStart\":\"<handler>\",\"expressionEnd\":\"<handler>\"},\"instance\":{\"moduleImp\":{\"jsonTypeInfo\":\"HANDLER\",\"implementationId\":10,\"author\":\"a@gmail.com\",\"name\":\"handler_10\",\"configurationRoot\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<dummy>\",\"expressionEnd\":\"<dummy>\"},\"value\":\"dummy\",\"dataType\":\"TEXT\",\"name\":\"dummy\",\"allowMultiple\":false}]},\"messageType\":{\"jsonTypeInfo\":\"MESSAGE_TYPE\",\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"instanceConfiguration\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<dummy>\",\"expressionEnd\":\"<dummy>\"},\"value\":\"dummy\",\"dataType\":\"TEXT\",\"name\":\"dummy\",\"allowMultiple\":false}]}},\"generics\":[{\"jsonTypeInfo\":\"MESSAGE_TYPE\",\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}}],\"type\":\"HANDLER\",\"name\":\"handler\",\"allowMultiple\":false}]},\"messageType\":{\"jsonTypeInfo\":\"MESSAGE_TYPE\",\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"instanceConfiguration\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"IMPLEMENTATION_SELECT\",\"keyExpression\":{\"expressionStart\":\"<handler>\",\"expressionEnd\":\"<handler>\"},\"instance\":{\"moduleImp\":{\"jsonTypeInfo\":\"HANDLER\",\"implementationId\":10,\"author\":\"a@gmail.com\",\"name\":\"handler_10\",\"configurationRoot\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<dummy>\",\"expressionEnd\":\"<dummy>\"},\"value\":\"dummy\",\"dataType\":\"TEXT\",\"name\":\"dummy\",\"allowMultiple\":false}]},\"messageType\":{\"jsonTypeInfo\":\"MESSAGE_TYPE\",\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"instanceConfiguration\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<dummy>\",\"expressionEnd\":\"<dummy>\"},\"value\":\"dummy\",\"dataType\":\"TEXT\",\"name\":\"dummy\",\"allowMultiple\":false}]}},\"generics\":[{\"jsonTypeInfo\":\"MESSAGE_TYPE\",\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}}],\"type\":\"HANDLER\",\"name\":\"handler\",\"allowMultiple\":false}]}},\"generics\":[{\"jsonTypeInfo\":\"MESSAGE_TYPE\",\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}}],\"type\":\"DISPATCHER\",\"name\":\"dispatcher\",\"allowMultiple\":false}]},{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<port>\",\"expressionEnd\":\"<port>\"},\"value\":\"8080\",\"dataType\":\"NUMBER\",\"name\":\"port\",\"allowMultiple\":false}]}", result);
    }

    @Test
    void testImplementation() throws JsonProcessingException {
        String result = new ObjectMapper().writeValueAsString(deserializer);
        //System.out.println(result);
        assertEquals("{\"jsonTypeInfo\":\"DESERIALIZER\",\"implementationId\":7,\"author\":\"a@gmail.com\",\"name\":\"deserializer_7\",\"configurationRoot\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<dummy>\",\"expressionEnd\":\"<dummy>\"},\"value\":\"dummy\",\"dataType\":\"TEXT\",\"name\":\"dummy\",\"allowMultiple\":false}]},\"protocolType\":{\"jsonTypeInfo\":\"PROTOCOL_TYPE\",\"implementationId\":0,\"author\":\"a@gmail.com\",\"name\":\"protocolType_0\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"messageType\":{\"jsonTypeInfo\":\"MESSAGE_TYPE\",\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}}", result);
    }

    @Test
    void testImpTypesToJson() throws JsonProcessingException {
        String result = new ObjectMapper().writeValueAsString(ImpType.values());
        //System.out.println(result);
        assertEquals("[{\"name\":\"ASSET_CONNECTION\",\"topLevel\":true,\"maxUse\":-1},{\"name\":\"DESERIALIZER\",\"topLevel\":false,\"maxUse\":-1},{\"name\":\"DISPATCHER\",\"topLevel\":false,\"maxUse\":-1},{\"name\":\"ENDPOINT\",\"topLevel\":true,\"maxUse\":-1},{\"name\":\"HANDLER\",\"topLevel\":false,\"maxUse\":-1},{\"name\":\"INTERACTION_STARTER\",\"topLevel\":true,\"maxUse\":-1},{\"name\":\"PERSISTENCE_MANAGER\",\"topLevel\":true,\"maxUse\":1},{\"name\":\"SERIALIZER\",\"topLevel\":false,\"maxUse\":1},{\"name\":\"PROTOCOL_TYPE\",\"topLevel\":false,\"maxUse\":0},{\"name\":\"MESSAGE_TYPE\",\"topLevel\":false,\"maxUse\":0},{\"name\":\"ENVIRONMENT\",\"topLevel\":true,\"maxUse\":1}]", result);
    }

    @Test
    void testEndpoint() throws JsonProcessingException {
        String result = new ObjectMapper().writeValueAsString(endpoint);
        //System.out.println(result);
        assertEquals("{\"jsonTypeInfo\":\"ENDPOINT\",\"implementationId\":11,\"author\":\"a@gmail.com\",\"name\":\"endpoint\",\"configurationRoot\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"<parent>\",\"expressionEnd\":\"<parent>\"},\"enforceCompatibility\":false,\"name\":\"parent\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"<mapping>\",\"expressionEnd\":\"<mapping>\"},\"enforceCompatibility\":true,\"name\":\"mapping\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<dummy>\",\"expressionEnd\":\"<dummy>\"},\"value\":\"dummy\",\"dataType\":\"TEXT\",\"name\":\"dummy\",\"allowMultiple\":false},{\"jsonTypeInfo\":\"IMPLEMENTATION_SELECT\",\"keyExpression\":{\"expressionStart\":\"<serializer>\",\"expressionEnd\":\"<serializer>\"},\"instance\":{\"moduleImp\":{\"jsonTypeInfo\":\"SERIALIZER\",\"implementationId\":8,\"author\":\"a@gmail.com\",\"name\":\"serializer_8\",\"configurationRoot\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<dummy>\",\"expressionEnd\":\"<dummy>\"},\"value\":\"dummy\",\"dataType\":\"TEXT\",\"name\":\"dummy\",\"allowMultiple\":false}]},\"protocolType\":{\"jsonTypeInfo\":\"PROTOCOL_TYPE\",\"implementationId\":0,\"author\":\"a@gmail.com\",\"name\":\"protocolType_0\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"messageType\":{\"jsonTypeInfo\":\"MESSAGE_TYPE\",\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"instanceConfiguration\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<dummy>\",\"expressionEnd\":\"<dummy>\"},\"value\":\"dummy\",\"dataType\":\"TEXT\",\"name\":\"dummy\",\"allowMultiple\":false}]}},\"generics\":[{\"jsonTypeInfo\":\"MESSAGE_TYPE\",\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},{\"jsonTypeInfo\":\"PROTOCOL_TYPE\",\"implementationId\":0,\"author\":\"a@gmail.com\",\"name\":\"protocolType_0\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}}],\"type\":\"SERIALIZER\",\"name\":\"serializer\",\"allowMultiple\":false},{\"jsonTypeInfo\":\"IMPLEMENTATION_SELECT\",\"keyExpression\":{\"expressionStart\":\"<deserializer>\",\"expressionEnd\":\"<deserializer>\"},\"instance\":{\"moduleImp\":{\"jsonTypeInfo\":\"DESERIALIZER\",\"implementationId\":7,\"author\":\"a@gmail.com\",\"name\":\"deserializer_7\",\"configurationRoot\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<dummy>\",\"expressionEnd\":\"<dummy>\"},\"value\":\"dummy\",\"dataType\":\"TEXT\",\"name\":\"dummy\",\"allowMultiple\":false}]},\"protocolType\":{\"jsonTypeInfo\":\"PROTOCOL_TYPE\",\"implementationId\":0,\"author\":\"a@gmail.com\",\"name\":\"protocolType_0\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"messageType\":{\"jsonTypeInfo\":\"MESSAGE_TYPE\",\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"instanceConfiguration\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<dummy>\",\"expressionEnd\":\"<dummy>\"},\"value\":\"dummy\",\"dataType\":\"TEXT\",\"name\":\"dummy\",\"allowMultiple\":false}]}},\"generics\":[{\"jsonTypeInfo\":\"MESSAGE_TYPE\",\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},{\"jsonTypeInfo\":\"PROTOCOL_TYPE\",\"implementationId\":0,\"author\":\"a@gmail.com\",\"name\":\"protocolType_0\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}}],\"type\":\"DESERIALIZER\",\"name\":\"deserializer\",\"allowMultiple\":false},{\"jsonTypeInfo\":\"IMPLEMENTATION_SELECT\",\"keyExpression\":{\"expressionStart\":\"<dispatcher>\",\"expressionEnd\":\"<dispatcher>\"},\"instance\":{\"moduleImp\":{\"jsonTypeInfo\":\"DISPATCHER\",\"implementationId\":9,\"author\":\"a@gmail.com\",\"name\":\"dispatcher_9\",\"configurationRoot\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"IMPLEMENTATION_SELECT\",\"keyExpression\":{\"expressionStart\":\"<handler>\",\"expressionEnd\":\"<handler>\"},\"instance\":{\"moduleImp\":{\"jsonTypeInfo\":\"HANDLER\",\"implementationId\":10,\"author\":\"a@gmail.com\",\"name\":\"handler_10\",\"configurationRoot\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<dummy>\",\"expressionEnd\":\"<dummy>\"},\"value\":\"dummy\",\"dataType\":\"TEXT\",\"name\":\"dummy\",\"allowMultiple\":false}]},\"messageType\":{\"jsonTypeInfo\":\"MESSAGE_TYPE\",\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"instanceConfiguration\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<dummy>\",\"expressionEnd\":\"<dummy>\"},\"value\":\"dummy\",\"dataType\":\"TEXT\",\"name\":\"dummy\",\"allowMultiple\":false}]}},\"generics\":[{\"jsonTypeInfo\":\"MESSAGE_TYPE\",\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}}],\"type\":\"HANDLER\",\"name\":\"handler\",\"allowMultiple\":false}]},\"messageType\":{\"jsonTypeInfo\":\"MESSAGE_TYPE\",\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"instanceConfiguration\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"IMPLEMENTATION_SELECT\",\"keyExpression\":{\"expressionStart\":\"<handler>\",\"expressionEnd\":\"<handler>\"},\"instance\":{\"moduleImp\":{\"jsonTypeInfo\":\"HANDLER\",\"implementationId\":10,\"author\":\"a@gmail.com\",\"name\":\"handler_10\",\"configurationRoot\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<dummy>\",\"expressionEnd\":\"<dummy>\"},\"value\":\"dummy\",\"dataType\":\"TEXT\",\"name\":\"dummy\",\"allowMultiple\":false}]},\"messageType\":{\"jsonTypeInfo\":\"MESSAGE_TYPE\",\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"instanceConfiguration\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<dummy>\",\"expressionEnd\":\"<dummy>\"},\"value\":\"dummy\",\"dataType\":\"TEXT\",\"name\":\"dummy\",\"allowMultiple\":false}]}},\"generics\":[{\"jsonTypeInfo\":\"MESSAGE_TYPE\",\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}}],\"type\":\"HANDLER\",\"name\":\"handler\",\"allowMultiple\":false}]}},\"generics\":[{\"jsonTypeInfo\":\"MESSAGE_TYPE\",\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}}],\"type\":\"DISPATCHER\",\"name\":\"dispatcher\",\"allowMultiple\":false}]},{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<port>\",\"expressionEnd\":\"<port>\"},\"value\":\"8080\",\"dataType\":\"NUMBER\",\"name\":\"port\",\"allowMultiple\":false}]},\"protocolType\":{\"jsonTypeInfo\":\"PROTOCOL_TYPE\",\"implementationId\":0,\"author\":\"a@gmail.com\",\"name\":\"protocolType_0\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}}",result);
    }

    @Test
    void testGeneric() throws JsonProcessingException {
        String result = new ObjectMapper().writeValueAsString(protocolTypeA);
        //System.out.println(result);
        assertEquals("{\"jsonTypeInfo\":\"PROTOCOL_TYPE\",\"implementationId\":0,\"author\":\"a@gmail.com\",\"name\":\"protocolType_0\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}}", result);
    }

    @Test
    void testPersistenceManager() throws JsonProcessingException {
        PersistenceManager manager = new PersistenceManager(13, userA, "manager", ImplementationScope.SHARED, dummy);
        String result = new ObjectMapper().writeValueAsString(manager);
        //System.out.println(result);
        assertEquals("{\"jsonTypeInfo\":\"PERSISTENCE_MANAGER\",\"implementationId\":13,\"author\":\"a@gmail.com\",\"name\":\"manager\",\"configurationRoot\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<dummy>\",\"expressionEnd\":\"<dummy>\"},\"value\":\"dummy\",\"dataType\":\"TEXT\",\"name\":\"dummy\",\"allowMultiple\":false}]},\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}}", result);
    }

    @Test
    void testConfigType() throws JsonProcessingException {
        String result = new ObjectMapper().writeValueAsString(ConfigurationType.values());
        //System.out.println(result);
        assertEquals("[{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"\",\"allowMultiple\":false,\"children\":[]},{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"dataType\":\"TEXT\",\"name\":\"\",\"allowMultiple\":false},{\"jsonTypeInfo\":\"IMPLEMENTATION_SELECT\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"generics\":[],\"type\":\"SERIALIZER\",\"name\":\"\",\"allowMultiple\":false},{\"jsonTypeInfo\":\"SELECT\",\"name\":\"\",\"allowMultiple\":false,\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"content\":{}},{\"jsonTypeInfo\":\"FILE_FIELD\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"name\":\"\",\"allowMultiple\":false},{\"jsonTypeInfo\":\"SWITCH\",\"name\":\"\",\"allowMultiple\":false,\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"content\":{\"falseValue\":\"\",\"trueValue\":\"\"},\"value\":\"\",\"valueKey\":\"falseValue\"}]",result);
    }

    @Test
    void testSelectToJsonWithValueSet() throws JsonProcessingException {
        Map<String, String> content = new HashMap<>();
        content.put("value1", "1");
        content.put("value2", "2");
        Select select = new Select("name", false, new KeyExpression("<select>", "<select>"), content);
        select.setValue("value1");
        String result = new ObjectMapper().writeValueAsString(select);
        System.out.println(result);
        assertEquals("{\"jsonTypeInfo\":\"SELECT\",\"name\":\"name\",\"allowMultiple\":false,\"keyExpression\":{\"expressionStart\":\"<select>\",\"expressionEnd\":\"<select>\"},\"content\":{\"value2\":\"2\",\"value1\":\"1\"},\"value\":\"1\",\"valueKey\":\"value1\"}", result);
        Select copy = new ObjectMapper().readValue(result, Select.class);
        System.out.println(copy.toString());
        assertEquals(select, copy);
    }

    @Test
    void testSelectToJsonWithoutValueSet() throws JsonProcessingException {
        Map<String, String> content = new HashMap<>();
        content.put("value1", "1");
        content.put("value2", "2");
        Select select = new Select("name", false, new KeyExpression("<select>", "<select>"), content);
        String result = new ObjectMapper().writeValueAsString(select);
        System.out.println(result);
        assertEquals("{\"jsonTypeInfo\":\"SELECT\",\"name\":\"name\",\"allowMultiple\":false,\"keyExpression\":{\"expressionStart\":\"<select>\",\"expressionEnd\":\"<select>\"},\"content\":{\"value2\":\"2\",\"value1\":\"1\"}}", result);
        Select copy = new ObjectMapper().readValue(result, Select.class);
        System.out.println(copy.toString());
        assertEquals(select, copy);
    }

    @Test
    void testFileField() throws JsonProcessingException {
        FileField field = new FileField("fileField", false, new KeyExpression("start", "end"));
        String result = new ObjectMapper().writeValueAsString(field);
        //System.out.println(result);
        assertEquals("{\"jsonTypeInfo\":\"FILE_FIELD\",\"keyExpression\":{\"expressionStart\":\"start\",\"expressionEnd\":\"end\"},\"name\":\"fileField\",\"allowMultiple\":false}", result);
        FileField copy = new ObjectMapper().readValue(result, FileField.class);
        //System.out.println(copy.toString());
        assertEquals(field, copy);
    }

    @Test
    void testSwitchToJson() throws JsonProcessingException {
        Switch switch_ = new Switch("name", false, new KeyExpression("<switch>", "<switch>"), "true", "false");
        String result = new ObjectMapper().writeValueAsString(switch_);
        System.out.println(result);
        assertEquals("{\"jsonTypeInfo\":\"SWITCH\",\"name\":\"name\",\"allowMultiple\":false,\"keyExpression\":{\"expressionStart\":\"<switch>\",\"expressionEnd\":\"<switch>\"},\"content\":{\"falseValue\":\"false\",\"trueValue\":\"true\"},\"value\":\"false\",\"valueKey\":\"falseValue\"}", result);
        result = "{\"jsonTypeInfo\":\"SWITCH\",\"name\":\"name\",\"allowMultiple\":false,\"keyExpression\":{\"expressionStart\":\"<switch>\",\"expressionEnd\":\"<switch>\"},\"content\":{\"falseValue\":\"false\",\"trueValue\":\"true\"},\"value\":\"false\",\"valueKey\":\"trueValue\"}";
        Switch copy = new ObjectMapper().readValue(result, Switch.class);
        switch_.setValue("trueValue");
        System.out.println(copy.toString());
        assertEquals(switch_, copy);
    }

    @Test
    void testMultipleImps() throws JsonProcessingException {
        PersistenceManager manager = new PersistenceManager(13, userA, "manager", ImplementationScope.SHARED, dummy);
        List<Implementation> imps = new ArrayList<>();
        imps.add(manager);
        imps.add(deserializer);
        String result = new ObjectMapper().writeValueAsString(imps);
        //System.out.println(result);
        assertEquals("[{\"implementationId\":13,\"author\":\"a@gmail.com\",\"name\":\"manager\",\"configurationRoot\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<dummy>\",\"expressionEnd\":\"<dummy>\"},\"value\":\"dummy\",\"dataType\":\"TEXT\",\"name\":\"dummy\",\"allowMultiple\":false}]},\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},{\"implementationId\":7,\"author\":\"a@gmail.com\",\"name\":\"deserializer_7\",\"configurationRoot\":{\"jsonTypeInfo\":\"CONFIGURATION_AGGREGATE\",\"keyExpression\":{\"expressionStart\":\"\",\"expressionEnd\":\"\"},\"enforceCompatibility\":false,\"name\":\"root\",\"allowMultiple\":false,\"children\":[{\"jsonTypeInfo\":\"TEXT_FIELD\",\"keyExpression\":{\"expressionStart\":\"<dummy>\",\"expressionEnd\":\"<dummy>\"},\"value\":\"dummy\",\"dataType\":\"TEXT\",\"name\":\"dummy\",\"allowMultiple\":false}]},\"protocolType\":{\"jsonTypeInfo\":\"PROTOCOL_TYPE\",\"implementationId\":0,\"author\":\"a@gmail.com\",\"name\":\"protocolType_0\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"messageType\":{\"jsonTypeInfo\":\"MESSAGE_TYPE\",\"implementationId\":3,\"author\":\"a@gmail.com\",\"name\":\"messageType_3\",\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}},\"scope\":{\"scopeId\":0,\"impScope\":\"SHARED\"}}]", result);
    }

    @Test
    void dataTypesToJson() throws JsonProcessingException {
        String result = new ObjectMapper().writeValueAsString(DataType.values());
        //System.out.println(result);
        assertEquals("[\"TEXT\",\"NUMBER\",\"EMAIL\",\"PASSWORD\",\"DATE\",\"DATETIME\"]", result);
    }

    @Test
    void testRequest() throws JsonProcessingException {
        RegisterRequest request = new RegisterRequest();
        request.setSecurityAnswer("test");
        request.setSecurityQuestion(SecurityQuestion.MAIDEN_NAME);
        request.setEmail("eskavi@web.de");
        request.setPassword("password");

        String result = new ObjectMapper().writeValueAsString(request);
        System.out.println(result);

        RegisterRequest copy = new ObjectMapper().readValue(result, RegisterRequest.class);
        System.out.println(copy.toString());
    }
}
