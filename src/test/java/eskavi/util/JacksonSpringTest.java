package eskavi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eskavi.model.configuration.*;
import eskavi.model.implementation.*;
import eskavi.model.implementation.moduleimp.*;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
import eskavi.repository.ImplementationRepository;
import eskavi.repository.UserRepository;
import eskavi.service.ImpService;
import eskavi.service.UserManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JacksonSpringTest {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ImplementationRepository impRepo;

    private ImpService impService;
    private UserManagementService userManagementService;
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
        userManagementService = new UserManagementService(userRepo);
        impService = new ImpService(impRepo, userRepo);
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
    void testMultiple() throws JsonProcessingException {
        userRepo.save(userA);
        impService.addImplementation(protocolTypeA, userA.getEmailAddress());
        impService.addImplementation(endpoint, userA.getEmailAddress());
        String result = new ObjectMapper().writeValueAsString(impService.getImps());
        System.out.println(result);
    }
}
