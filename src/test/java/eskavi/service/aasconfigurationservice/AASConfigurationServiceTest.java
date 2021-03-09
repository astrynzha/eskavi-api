package eskavi.service.aasconfigurationservice;


import eskavi.model.configuration.Configuration;
import eskavi.model.configuration.ConfigurationAggregate;
import eskavi.model.configuration.KeyExpression;
import eskavi.model.implementation.ImplementationScope;
import eskavi.model.implementation.MessageType;
import eskavi.model.implementation.ProtocolType;
import eskavi.model.implementation.moduleimp.Serializer;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.repository.ImplementationRepository;
import eskavi.repository.UserRepository;
import eskavi.service.ImpService;
import eskavi.service.UserManagementService;
import eskavi.util.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AASConfigurationServiceTest {
    @Autowired
    private ImplementationRepository impRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    Config config;

    private AASConfigurationService aasService;
    private ImpService impService;
    private UserManagementService userManagementService;
    private String someEmail;
    private Serializer serializer;
    private Configuration configuration1;
    private Configuration configuration2;
    private ProtocolType protocolType;
    private MessageType messageType;

    @BeforeEach
    void setUp() {
        impService = new ImpService(impRepo, userRepo, config);
        aasService = new AASConfigurationService(impRepo, userRepo,
                new AASSessionHandler());
        userManagementService = new UserManagementService(userRepo);
        someEmail = "a.str@gmail.com";
        userManagementService.createUser(someEmail, "dka;fj",
                SecurityQuestion.MAIDEN_NAME, "Bezos");
        configuration1 = new ConfigurationAggregate("first", false,
                new KeyExpression("<text>", "<text>"), new LinkedList<>(), false);
        configuration2 = new ConfigurationAggregate("second", false,
                new KeyExpression("<text>", "<text>"), new LinkedList<>(), false);
        protocolType = new ProtocolType(0, (User) userManagementService.getUser(someEmail),
                "protocolType_0", ImplementationScope.SHARED);
        messageType = new MessageType(3, (User) userManagementService.getUser(someEmail),
                "messageType_3", ImplementationScope.SHARED);
        messageType = (MessageType) impService.addImplementation(messageType, someEmail);
        protocolType = (ProtocolType) impService.addImplementation(protocolType, someEmail);
        serializer = new Serializer(8, (User) userManagementService.getUser(someEmail),
                "serializer_8", ImplementationScope.SHARED, configuration1, messageType, protocolType);
    }


    @Test
    void createAASConstructionSession() {
        long id1 = aasService.createAASConstructionSession(someEmail);
        assertNotNull(aasService.generateJavaClass(id1));
        long id2 = aasService.createAASConstructionSession(someEmail);
        assertNotNull(aasService.generateJavaClass(id2));
    }

    @Test
    void removeAASConstructionSession() {
        long id = aasService.createAASConstructionSession(someEmail);
        aasService.removeAASConstructionSession(id);
        assertThrows(ResponseStatusException.class, () -> aasService.generateJavaClass(id));
    }

    @Test
    void addRegistry() {
        long id1 = aasService.createAASConstructionSession(someEmail);
        List<String> registry = new LinkedList<>(Arrays.asList("https://google.com", "https://eskavi.com"));
        aasService.addRegistry(id1, registry);
    }

    // test addModuleInstance & getConfiguration
    @Test
    void addModuleInstance() {
        // add serializer to some session
        serializer = (Serializer) impService.addImplementation(serializer, someEmail);
        long sessionId = aasService.createAASConstructionSession(someEmail);
        aasService.addModuleInstance(sessionId, serializer.getImplementationId());
        // test if serializer is there
        assertNotNull(aasService.getConfiguration(sessionId, serializer.getImplementationId()));
    }

    // TODO more tests with more sophisticated configurations & test if they are really updated. In der Testphase?
    @Test
    void updateConfiguration() {
        // add serializer to some session
        serializer = (Serializer) impService.addImplementation(serializer, someEmail);
        long sessionId = aasService.createAASConstructionSession(someEmail);
        aasService.addModuleInstance(sessionId, serializer.getImplementationId());
        Configuration conf = aasService.getConfiguration(sessionId, serializer.getImplementationId());
        assertEquals(configuration1.getName(), conf.getName());

        // test that changing configuration name causes an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                aasService.updateConfiguration(sessionId, configuration2, serializer.getImplementationId()));
        assertTrue(exception.getMessage().contains("Configuration has to match the template configuration"));
        // test that the configuration was not changed
        conf = aasService.getConfiguration(sessionId, serializer.getImplementationId());
        assertEquals(configuration1.getName(), conf.getName());
    }

    @Test
    void removeModuleInstance() {
        serializer = (Serializer) impService.addImplementation(serializer, someEmail);
        long sessionId = aasService.createAASConstructionSession(someEmail);
        aasService.addModuleInstance(sessionId, serializer.getImplementationId());
        aasService.removeModuleInstance(sessionId, serializer.getImplementationId());
        // exception thrown -> Module instance not found
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> aasService.getConfiguration(sessionId, serializer.getImplementationId()));
        assertTrue(exception.getMessage().contains("ModuleInstance " + serializer.getImplementationId() +
                " is not found in the constructions session " + sessionId));
    }


    @Test
    void generateJavaClass() {
        // TODO
    }
}