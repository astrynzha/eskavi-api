package eskavi.service.aasconfigurationservice;


import eskavi.model.configuration.Configuration;
import eskavi.model.configuration.ConfigurationAggregate;
import eskavi.model.configuration.DataType;
import eskavi.model.configuration.KeyExpression;
import eskavi.model.configuration.TextField;
import eskavi.model.implementation.ImplementationScope;
import eskavi.model.implementation.MessageType;
import eskavi.model.implementation.ProtocolType;
import eskavi.model.implementation.moduleimp.Serializer;
import eskavi.model.user.User;
import eskavi.service.ImpService;
import eskavi.service.UserManagementService;
import eskavi.service.mockrepo.MockImplementationRepository;
import eskavi.service.mockrepo.MockUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class AASConfigurationServiceTest {

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
        MockUserRepository userRepo = new MockUserRepository(); // TODO: replace
        MockImplementationRepository impRepo = new MockImplementationRepository(); // TODO: replace
        impService = new ImpService(impRepo, userRepo);
        aasService = new AASConfigurationService(impRepo, userRepo,
                new AASSessionHandler()); // TODO: replace
        userManagementService = new UserManagementService(userRepo);
        someEmail = "a.str@gmail.com";
        userManagementService.createUser(someEmail, "dka;fj");
        configuration1 = new ConfigurationAggregate("first", false,
                new KeyExpression("<text>", "<text>"), new LinkedList<>(), false);
        configuration2 = new ConfigurationAggregate("second", false,
                new KeyExpression("<text>", "<text>"), new LinkedList<>(), false);
        protocolType = new ProtocolType(0, (User) userManagementService.getUser(someEmail),
                "protocolType_0", ImplementationScope.SHARED);
        messageType = new MessageType(3, (User) userManagementService.getUser(someEmail),
                "messageType_3", ImplementationScope.SHARED);
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

    // test addModuleInstance & getConfiguration
    @Test
    void addModuleInstance() {
        // add serializer to some session
        impService.addImplementation(serializer, someEmail);
        long sessionId = aasService.createAASConstructionSession(someEmail);
        aasService.addModuleInstance(sessionId, serializer.getImplementationId());
        // test if serializer is there
        assertNotNull(aasService.getConfiguration(sessionId, serializer.getImplementationId()));
    }

    // TODO more tests with more sophisticated configurations & test if they are really updated. In der Testphase?
    @Test
    void updateConfiguration() {
        // add serializer to some session
        impService.addImplementation(serializer, someEmail);
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
        impService.addImplementation(serializer, someEmail);
        long sessionId  = aasService.createAASConstructionSession(someEmail);
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