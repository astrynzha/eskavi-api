package eskavi.persistance;

import eskavi.model.configuration.Configuration;
import eskavi.model.configuration.DataType;
import eskavi.model.configuration.KeyExpression;
import eskavi.model.configuration.TextField;
import eskavi.model.implementation.Implementation;
import eskavi.model.implementation.ImplementationScope;
import eskavi.model.implementation.ProtocolType;
import eskavi.model.implementation.moduleimp.Endpoint;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
import eskavi.repository.ImplementationRepository;
import eskavi.repository.ScopeRepository;
import eskavi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static junit.framework.TestCase.assertEquals;

@SpringBootTest(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
@DirtiesContext
public class ImplementationRepositoryTest {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ImplementationRepository impRepo;
    @Autowired
    private ScopeRepository scopeRepo;

    private ProtocolType protocolTypeA;
    private Endpoint endpoint;
    private Configuration trueConfiguration;
    private User userA;


    @BeforeEach
    public void setUp() {
        trueConfiguration = new TextField("text", false,
                new KeyExpression("<text>", "<text>"), DataType.TEXT);
        userA = new User("a@gmail.com", "dfjask;fj",
                UserLevel.PUBLISHING_USER, SecurityQuestion.MAIDEN_NAME, "Julia");
        protocolTypeA = new ProtocolType(0, userA, "protocolType_0", ImplementationScope.SHARED);
        endpoint = new Endpoint(1, userA, "endpoint_1", ImplementationScope.SHARED, trueConfiguration, protocolTypeA);
    }

    @Test
    void happyPath() throws IllegalAccessException {

        userRepo.save(userA);
        endpoint = impRepo.save(endpoint);
        Implementation implementation = impRepo.findById(endpoint.getImplementationId()).get();
        userA.subscribe(implementation);
        assertEquals(implementation, endpoint);
    }
}
