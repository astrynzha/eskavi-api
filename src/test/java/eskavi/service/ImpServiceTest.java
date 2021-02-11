package eskavi.service;

import eskavi.model.configuration.Configuration;
import eskavi.model.configuration.DataType;
import eskavi.model.configuration.KeyExpression;
import eskavi.model.configuration.TextField;
import eskavi.model.implementation.ImmutableModuleImp;
import eskavi.model.implementation.ImplementationScope;
import eskavi.model.implementation.MessageType;
import eskavi.model.implementation.ProtocolType;
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
import eskavi.service.mockrepo.MockImplementationRepository;
import eskavi.service.mockrepo.MockUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImpServiceTest {
    private ImpService impService;
    private UserManagementService userService;
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
    private User userA;
    private User userB;


    @BeforeEach
    void setUp() {
        MockUserRepository userRepository = new MockUserRepository();
        MockImplementationRepository implementationRepository = new MockImplementationRepository();
        impService = new ImpService(implementationRepository, userRepository);
        userService = new UserManagementService(userRepository);

        userA = new User("a@gmail.com", "dfjask;fj",
                UserLevel.PUBLISHING_USER, SecurityQuestion.MAIDEN_NAME, "Julia");
        userB = new User("b@gmail.com", "dfjask;fj",
                UserLevel.PUBLISHING_USER, SecurityQuestion.MAIDEN_NAME, "Julia");
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
    void getImp() {
    }

    @Test
    void getImps() {
    }

    @Test
    void testGetImps() {
    }

    @Test
    void getDefaultImpCreate() {
    }

    @Test
    void addImplementation() {
        userService.createUser("a.str@gmail.com", "jasdjfk3");
        impService.addImplementation(endpoint, "a.str@gmail.com");
        assertEquals(impService.getImp(endpoint.getImplementationId()), endpoint);
    }

    @Test
    void addUser() {
    }

    @Test
    void removeUser() {
    }

    @Test
    void getUsers() {
    }

    @Test
    void updateImplementation() {
    }

    @Test
    void removeImplementation() {
    }
}