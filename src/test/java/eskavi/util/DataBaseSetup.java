package eskavi.util;

import eskavi.model.configuration.Configuration;
import eskavi.model.configuration.DataType;
import eskavi.model.configuration.KeyExpression;
import eskavi.model.configuration.TextField;
import eskavi.model.implementation.ImplementationScope;
import eskavi.model.implementation.MessageType;
import eskavi.model.implementation.ProtocolType;
import eskavi.model.implementation.moduleimp.*;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
import eskavi.repository.ImplementationRepository;
import eskavi.repository.UserRepository;
import eskavi.service.UserManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class DataBaseSetup {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImplementationRepository implementationRepository;
    @Test
    void setUp() {
        User admin = new User("admin@eskavi.com", new BCryptPasswordEncoder().encode("admin"), UserLevel.ADMINISTRATOR, SecurityQuestion.MAIDEN_NAME, "julia");
        userRepository.save(admin);
        Configuration dummy = new TextField("dummy", false, new KeyExpression("", ""), DataType.TEXT);
        MessageType messageType = new MessageType(1, admin, "defaultMessageType", ImplementationScope.PRIVATE);
        messageType = implementationRepository.save(messageType);

        ProtocolType protocolType = new ProtocolType(2, admin, "defaultProtocolType", ImplementationScope.PRIVATE);
        protocolType = implementationRepository.save(protocolType);

        AssetConnection assetConnection = new AssetConnection(3, admin, "defaultAssetConnection", ImplementationScope.PRIVATE, dummy);
        implementationRepository.save(assetConnection);

        InteractionStarter interactionStarter = new InteractionStarter(4, admin, "defaultInteractionStarter", ImplementationScope.PRIVATE, dummy);
        implementationRepository.save(interactionStarter);

        PersistenceManager persistenceManager = new PersistenceManager(5, admin, "defaultPersistenceManager", ImplementationScope.PRIVATE, dummy);
        implementationRepository.save(persistenceManager);

        Deserializer deserializer = new Deserializer(6, admin, "defaultDeserializer", ImplementationScope.PRIVATE, dummy, messageType, protocolType);
        implementationRepository.save(deserializer);

        Dispatcher dispatcher = new Dispatcher(7, admin, "defaultDispatcher", ImplementationScope.PRIVATE, dummy, messageType);
        implementationRepository.save(dispatcher);

        Endpoint endpoint = new Endpoint(8, admin, "defaultEndpoint", ImplementationScope.PRIVATE, dummy, protocolType);
        implementationRepository.save(endpoint);

        Serializer serializer = new Serializer(9, admin, "defaultSerializer", ImplementationScope.PRIVATE, dummy, messageType, protocolType);
        implementationRepository.save(serializer);

        Handler handler = new Handler(10, admin, "defaultHandler", ImplementationScope.PRIVATE, dummy, messageType);
        implementationRepository.save(handler);
    }
}
