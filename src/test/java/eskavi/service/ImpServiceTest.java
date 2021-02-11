package eskavi.service;

import eskavi.model.configuration.Configuration;
import eskavi.model.configuration.DataType;
import eskavi.model.configuration.KeyExpression;
import eskavi.model.configuration.TextField;
import eskavi.model.implementation.ImmutableImplementation;
import eskavi.model.implementation.ImmutableModuleImp;
import eskavi.model.implementation.Implementation;
import eskavi.model.implementation.ImplementationScope;
import eskavi.model.implementation.MessageType;
import eskavi.model.implementation.ProtocolType;
import eskavi.model.implementation.Scope;
import eskavi.model.implementation.moduleimp.AssetConnection;
import eskavi.model.implementation.moduleimp.Deserializer;
import eskavi.model.implementation.moduleimp.Dispatcher;
import eskavi.model.implementation.moduleimp.Endpoint;
import eskavi.model.implementation.moduleimp.Handler;
import eskavi.model.implementation.moduleimp.InteractionStarter;
import eskavi.model.implementation.moduleimp.PersistenceManager;
import eskavi.model.implementation.moduleimp.Serializer;
import eskavi.model.user.ImmutableUser;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
import eskavi.service.mockrepo.MockImplementationRepository;
import eskavi.service.mockrepo.MockUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.text.CollationElementIterator;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
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
    private ImmutableUser userA;
    private ImmutableUser userB;
    private ImmutableUser userC;


    @BeforeEach
    void setUp() {
        MockUserRepository userRepository = new MockUserRepository();
        MockImplementationRepository implementationRepository = new MockImplementationRepository();
        impService = new ImpService(implementationRepository, userRepository);
        userService = new UserManagementService(userRepository);
        userA = userService.createUser("a.str@gmail.com", "dja;lsfkdjsafk");
        userB = userService.createUser("str@gmail.com", "dsa;lfj[b");
        userC = userService.createUser("str1@gmail.com", "dsa;lfj[b");


        trueConfiguration = new TextField("text", false,
                new KeyExpression("<text>", "<text>"), DataType.TEXT);
        protocolTypeA = new ProtocolType(0, (User) userA, "protocolType_0", ImplementationScope.SHARED);
        protocolTypeB = new ProtocolType(4, (User) userA, "protocolType_4", ImplementationScope.SHARED);
        messageTypeA = new MessageType(3, (User) userA, "messageType_3", ImplementationScope.SHARED);
        messageTypeB = new MessageType(5, (User) userA, "messageType_5", ImplementationScope.SHARED);
        endpoint = new Endpoint(1, (User) userA, "endpoint_1", ImplementationScope.SHARED, trueConfiguration, protocolTypeA);
        assetConnection = new AssetConnection(6, (User) userA, "assetconnection", ImplementationScope.PUBLIC, trueConfiguration);
        deserializer = new Deserializer(7, (User) userA, "deserializer_7",
                ImplementationScope.SHARED, trueConfiguration, messageTypeA, protocolTypeA);
        serializer = new Serializer(8, (User) userA,
                "serializer_8", ImplementationScope.SHARED, trueConfiguration, messageTypeA, protocolTypeA);
        dispatcher = new Dispatcher(9, (User) userA, "dispatcher_9", ImplementationScope.SHARED, trueConfiguration, messageTypeA);
        handler = new Handler(10, (User) userA, "handler_10", ImplementationScope.SHARED, trueConfiguration, messageTypeA);
        interactionStarter = new InteractionStarter(11, (User) userA,
                "interactionStarter11", ImplementationScope.SHARED, trueConfiguration);
        persistenceManager = new PersistenceManager(12, (User) userA,
                "persistanceManager_12", ImplementationScope.SHARED, trueConfiguration);
        usedImpCollection = new LinkedList<>(Arrays.asList(endpoint, serializer, deserializer,
                dispatcher, handler, assetConnection, interactionStarter, persistenceManager));
    }

    @Test
    void getImps() {
        impService.addImplementation(endpoint, "str@gmail.com");
        impService.addImplementation(deserializer, "a.str@gmail.com");
        Collection<ImmutableImplementation> imps = impService.getImps();
        assertEquals(impService.getImps().size(), 2);
        assertTrue(imps.contains(endpoint));
        assertTrue(imps.contains(deserializer));
    }

    @Test
    void getDefaultImpCreate() {
    }

    @Test
    void addImplementation() {
        impService.addImplementation(endpoint, "a.str@gmail.com");
        assertEquals(impService.getImp(endpoint.getImplementationId()), endpoint);
    }

    @Test
    void addUser() {
        impService.addImplementation(endpoint, "a.str@gmail.com");
        // addUser()
        try {
            impService.addUser(endpoint.getImplementationId(), "str@gmail.com", "a.str@gmail.com");
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }
        //addUser() with callerId != authorId
        assertThrows(ResponseStatusException.class,
                () -> impService.addUser(endpoint.getImplementationId(), "str@gmail.com", "str@gmail.com"));

        // change scope to private + update repo
        endpoint.setScope(new Scope(ImplementationScope.PRIVATE));
        try {
            impService.updateImplementation(endpoint, "a.str@gmail.com");
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }

        //addUser() with scope != SHARED
        Exception exception = assertThrows(IllegalAccessException.class,
                () -> impService.addUser(endpoint.getImplementationId(),
                        "str@gmail.com", "a.str@gmail.com"));
        assertTrue(exception.getMessage().contains("ImplementationScope is not SHARED"));
    }

    @Test
    void removeUser() {
        impService.addImplementation(endpoint, "a.str@gmail.com");
        try {
            impService.addUser(endpoint.getImplementationId(), "str@gmail.com", "a.str@gmail.com");
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }

        //removeUser() with callerId != authorId
        assertThrows(ResponseStatusException.class,
                () -> impService.removeUser(endpoint.getImplementationId(),
                        "str@gmail.com", "str@gmail.com"));

        try {
            impService.removeUser(endpoint.getImplementationId(), "str@gmail.com", "a.str@gmail.com");
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }

        //removeUser() trying to remove the author
        assertThrows(ResponseStatusException.class,
                () -> impService.removeUser(endpoint.getImplementationId(),
                        "a.str@gmail.com", "a.str@gmail.com"));
    }

    @Test
    void getUsers() {
        impService.addImplementation(endpoint, "a.str@gmail.com");
        try {
            impService.addUser(endpoint.getImplementationId(), "str@gmail.com", "a.str@gmail.com");
            impService.addUser(endpoint.getImplementationId(), "str1@gmail.com", "a.str@gmail.com");
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }
        Collection<ImmutableUser> users = impService.getUsers(endpoint.getImplementationId());
        assertEquals(3, users.size());
        assertTrue(users.contains(userB));
        assertTrue(users.contains(userC));
    }

    @Test
    void updateImplementation() {
        // TODO
    }

    @Test
    void removeImplementation() {
        impService.addImplementation(endpoint, "a.str@gmail.com");
        assertEquals(impService.getImp(endpoint.getImplementationId()), endpoint);

        // try to removeImplementation with caller != author
        Exception exception = assertThrows(IllegalAccessException.class,
                () -> impService.removeImplementation(endpoint.getImplementationId(), "str@gmail.com"));
        assertTrue(exception.getMessage()
                .contains("This user cannot remove the implementation, he is not it's author"));

        try {
            impService.removeImplementation(endpoint.getImplementationId(), "a.str@gmail.com");
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }
        Collection<ImmutableImplementation> imps = impService.getImps();
        assertTrue(imps.isEmpty());
    }
}