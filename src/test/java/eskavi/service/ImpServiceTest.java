package eskavi.service;

import eskavi.model.configuration.Configuration;
import eskavi.model.configuration.DataType;
import eskavi.model.configuration.KeyExpression;
import eskavi.model.configuration.TextField;
import eskavi.model.implementation.*;
import eskavi.model.implementation.moduleimp.*;
import eskavi.model.user.ImmutableUser;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.repository.ImplementationRepository;
import eskavi.repository.UserRepository;
import eskavi.util.Config;
import org.hibernate.annotations.Immutable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ImpServiceTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImplementationRepository implementationRepository;
    @Autowired
    Config config;

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
    private Configuration configuration;
    private Configuration configuration1;
    private Configuration configuration2;
    private Configuration configuration3;
    private Configuration configuration4;
    private Configuration configuration5;
    private Configuration configuration6;
    private Configuration configuration7;
    private Configuration configuration8;
    private ImmutableUser userA;
    private ImmutableUser userB;
    private ImmutableUser userC;


    @BeforeEach
    void setUp() {
        impService = new ImpService(implementationRepository, userRepository, config);
        userService = new UserManagementService(userRepository);
        userA = userService.createUser("a.str@gmail.com", "dja;lsfkdjsafk",
                SecurityQuestion.MAIDEN_NAME, "Bezos");
        userB = userService.createUser("str@gmail.com", "dsa;lfj[b",
                SecurityQuestion.MAIDEN_NAME, "Gates");
        userC = userService.createUser("str1@gmail.com", "dsa;lfj[b",
                SecurityQuestion.MAIDEN_NAME, "Zuck");


        configuration = new TextField("text", false,
                new KeyExpression("<text>", "<text>"), DataType.TEXT);
        configuration1 = new TextField("text", false,
                new KeyExpression("<text>", "<text>"), DataType.TEXT);
        configuration2 = new TextField("text", false,
                new KeyExpression("<text>", "<text>"), DataType.TEXT);
        configuration3 = new TextField("text", false,
                new KeyExpression("<text>", "<text>"), DataType.TEXT);
        configuration4 = new TextField("text", false,
                new KeyExpression("<text>", "<text>"), DataType.TEXT);
        configuration5 = new TextField("text", false,
                new KeyExpression("<text>", "<text>"), DataType.TEXT);
        configuration6 = new TextField("text", false,
                new KeyExpression("<text>", "<text>"), DataType.TEXT);
        configuration7 = new TextField("text", false,
                new KeyExpression("<text>", "<text>"), DataType.TEXT);
        configuration8 = new TextField("text", false,
                new KeyExpression("<text>", "<text>"), DataType.TEXT);
        protocolTypeA = new ProtocolType(0, (User) userA, "protocolType_0", ImplementationScope.SHARED);
        protocolTypeB = new ProtocolType(4, (User) userA, "protocolType_4", ImplementationScope.SHARED);
        messageTypeA = new MessageType(3, (User) userA, "messageType_3", ImplementationScope.SHARED);
        messageTypeB = new MessageType(5, (User) userA, "messageType_5", ImplementationScope.SHARED);
        protocolTypeA = (ProtocolType) impService.addImplementation(protocolTypeA, userA.getEmailAddress());
        messageTypeA = (MessageType) impService.addImplementation(messageTypeA, userA.getEmailAddress());
        endpoint = new Endpoint(1, (User) userA, "endpoint_1", ImplementationScope.SHARED, configuration, protocolTypeA);
        assetConnection = new AssetConnection(6, (User) userA, "assetconnection", ImplementationScope.PUBLIC, configuration1);
        deserializer = new Deserializer(7, (User) userA, "deserializer_7",
                ImplementationScope.SHARED, configuration2, messageTypeA, protocolTypeA);
        serializer = new Serializer(8, (User) userA,
                "serializer_8", ImplementationScope.SHARED, configuration3, messageTypeA, protocolTypeA);
        dispatcher = new Dispatcher(9, (User) userA, "dispatcher_9", ImplementationScope.SHARED, configuration4, messageTypeA);
        handler = new Handler(10, (User) userA, "handler_10", ImplementationScope.SHARED, configuration5, messageTypeA);
        interactionStarter = new InteractionStarter(11, (User) userA,
                "interactionStarter11", ImplementationScope.SHARED, configuration6);
        persistenceManager = new PersistenceManager(12, (User) userA,
                "persistanceManager_12", ImplementationScope.SHARED, configuration7);
        usedImpCollection = new LinkedList<>(Arrays.asList(endpoint, serializer, deserializer,
                dispatcher, handler, assetConnection, interactionStarter, persistenceManager));
    }

    @Test
    void getImps() {
        endpoint = (Endpoint) impService.addImplementation(endpoint, "str@gmail.com");
        Endpoint endpoint1 = new Endpoint(13, (User) userA,
                "endpoint13", ImplementationScope.SHARED, configuration8, protocolTypeA);
        endpoint1 = (Endpoint) impService.addImplementation(endpoint1, "a.str@gmail.com");
        deserializer = (Deserializer) impService.addImplementation(deserializer, "a.str@gmail.com");
        Collection<ImmutableImplementation> imps = impService.getImps();
        assertTrue(imps.contains(endpoint));
        assertTrue(imps.contains(deserializer));
        var imps1 = impService.getImps(ImpType.ENDPOINT, "str@gmail.com");
        assertTrue(imps1.contains(endpoint));
        assertFalse(imps1.contains(endpoint1));
    }

    // TODO test isValid()
    @Test
    void addImplementation() {
        endpoint = (Endpoint) impService.addImplementation(endpoint, "a.str@gmail.com");
        // scope contains author
        assertEquals(impService.getImp(endpoint.getImplementationId(), endpoint.getAuthor().getEmailAddress()),
                endpoint);
    }

    @Test
    void addImplementation1() {
        endpoint.setScope(new Scope(ImplementationScope.SHARED));
        endpoint = (Endpoint) impService.addImplementation(endpoint, "a.str@gmail.com");
        ImmutableImplementation i = impService.getImp(endpoint.getImplementationId(), endpoint.getAuthor().getEmailAddress());
        // scope is empty now
        assertEquals(i.getImplementationId(),
                endpoint.getImplementationId());
    }

    @Test
    void addUser() {
        endpoint = (Endpoint) impService.addImplementation(endpoint, "a.str@gmail.com");
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
        endpoint = (Endpoint) impService.addImplementation(endpoint, "a.str@gmail.com");
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
    void removeImplementation() {
        endpoint = (Endpoint) impService.addImplementation(endpoint, "a.str@gmail.com");
        assertEquals(impService.getImp(endpoint.getImplementationId(), endpoint.getAuthor().getEmailAddress()), endpoint);

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
        assertTrue(!imps.contains(endpoint));
    }

    @Test
    void updateImplementation() {
        // TODO
    }

    @Test
    void isValidTest() {
        // TODO
    }

    @Test
    void getDefaultImpCreate() {
        impService.addImplementation(endpoint, "a.str@gmail.com");
        impService.addImplementation(dispatcher, "a.str@gmail.com");
        impService.addImplementation(serializer, "a.str@gmail.com");
        impService.addImplementation(deserializer, "a.str@gmail.com");
        impService.addImplementation(assetConnection, "a.str@gmail.com");
        impService.addImplementation(handler, "a.str@gmail.com");
        impService.addImplementation(interactionStarter, "a.str@gmail.com");
        impService.addImplementation(persistenceManager, "a.str@gmail.com");
        impService.addImplementation(protocolTypeA, "a.str@gmail.com");
        impService.addImplementation(messageTypeA, "a.str@gmail.com");

        config.setENDPOINT(endpoint.getImplementationId());
        config.setDISPATCHER(dispatcher.getImplementationId());
        config.setSERIALIZER(serializer.getImplementationId());
        config.setDESERIALIZER(deserializer.getImplementationId());
        config.setASSET_CONNECTION(assetConnection.getImplementationId());
        config.setHANDLER(handler.getImplementationId());
        config.setINTERACTION_STARTER(interactionStarter.getImplementationId());
        config.setPERSISTENCE_MANAGER(persistenceManager.getImplementationId());
        config.setPROTOCOL_TYPE(protocolTypeA.getImplementationId());
        config.setMESSAGE_TYPE(messageTypeA.getImplementationId());


        ImmutableImplementation mi;

        mi = impService.getDefaultImpCreate(ImpType.ENDPOINT);
        assertEquals(endpoint.getName(), mi.getName());

        mi = impService.getDefaultImpCreate(ImpType.SERIALIZER);
        assertEquals(serializer.getName(), mi.getName());

        mi = impService.getDefaultImpCreate(ImpType.DESERIALIZER);
        assertEquals(deserializer.getName(), mi.getName());

        mi = impService.getDefaultImpCreate(ImpType.DISPATCHER);
        assertEquals(dispatcher.getName(), mi.getName());

        mi = impService.getDefaultImpCreate(ImpType.ASSET_CONNECTION);
        assertEquals(assetConnection.getName(), mi.getName());

        mi = impService.getDefaultImpCreate(ImpType.HANDLER);
        assertEquals(handler.getName(), mi.getName());

        mi = impService.getDefaultImpCreate(ImpType.INTERACTION_STARTER);
        assertEquals(interactionStarter.getName(), mi.getName());

        mi = impService.getDefaultImpCreate(ImpType.PERSISTENCE_MANAGER);
        assertEquals(persistenceManager.getName(), mi.getName());

        mi = impService.getDefaultImpCreate(ImpType.MESSAGE_TYPE);
        assertEquals(messageTypeA.getName(), mi.getName());

        mi = impService.getDefaultImpCreate(ImpType.PROTOCOL_TYPE);
        assertEquals(protocolTypeA.getName(), mi.getName());
    }

}
