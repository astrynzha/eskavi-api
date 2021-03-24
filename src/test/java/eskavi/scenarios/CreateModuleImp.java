package eskavi.scenarios;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import eskavi.EskaviApplication;
import eskavi.model.configuration.Configuration;
import eskavi.model.configuration.DataType;
import eskavi.model.configuration.KeyExpression;
import eskavi.model.configuration.TextField;
import eskavi.model.implementation.ImmutableImplementation;
import eskavi.model.implementation.ImplementationScope;
import eskavi.model.implementation.MessageType;
import eskavi.model.implementation.ProtocolType;
import eskavi.model.implementation.moduleimp.*;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
import eskavi.repository.ImplementationRepository;
import eskavi.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"}, classes = EskaviApplication.class)
@DirtiesContext
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateModuleImp {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ImplementationRepository impRepository;
    //dummyData
    User creator;
    String token;
    Configuration configuration;
    MessageType messageType;
    ProtocolType protocolType;
    //SpringBootComponents
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() throws Exception {
        creator = new User("a@gmail.com", new BCryptPasswordEncoder().encode("1234"),
                UserLevel.PUBLISHING_USER, SecurityQuestion.MAIDEN_NAME, "Julia");
        userRepository.save(creator);
        MvcResult result = mvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"email\":\"a@gmail.com\",\n" +
                        "\t\"password\":\"1234\"\n" +
                        "}"))
                .andReturn();
        token = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");
        configuration = new TextField("text", false, new KeyExpression("Text(", ");"), DataType.TEXT);

        messageType = impRepository.save(new MessageType(0, creator, "standardMessageType", ImplementationScope.PRIVATE));
        protocolType = impRepository.save(new ProtocolType(1, creator, "standardProtocolType", ImplementationScope.PRIVATE));
    }

    private void performPost(ImmutableImplementation implementation) throws Exception {
        String body = new ObjectMapper().writeValueAsString(implementation);
        mvc.perform(post("/api/imp")
                .header("Authorization", "Bearer " + token)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(100)
    void testMessageType() throws Exception {
        messageType = new MessageType(0, creator, "messageType", ImplementationScope.PRIVATE);
        performPost(messageType);
    }

    @Test
    @Order(200)
    void testProtocolType() throws Exception {
        protocolType = new ProtocolType(1, creator, "protocolType", ImplementationScope.PRIVATE);
        performPost(protocolType);
    }

    @Test
    @Order(300)
    void testAssetConnection() throws Exception {
        AssetConnection assetConnection = new AssetConnection(2, creator, "assetConnection", ImplementationScope.PRIVATE, configuration);
        performPost(assetConnection);
    }

    @Test
    @Order(400)
    void testDeserializer() throws Exception {
        Deserializer deserializer = new Deserializer(3, creator, "deserializer", ImplementationScope.PRIVATE, configuration, messageType, protocolType);
        performPost(deserializer);
    }

    @Test
    @Order(500)
    void testDispatcher() throws Exception {
        Dispatcher dispatcher = new Dispatcher(4, creator, "dispatcher", ImplementationScope.PRIVATE, configuration, messageType);
        performPost(dispatcher);
    }

    @Test
    @Order(600)
    void testEndpoint() throws Exception {
        Endpoint endpoint = new Endpoint(5, creator, "endpoint", ImplementationScope.PRIVATE, configuration, protocolType);
        performPost(endpoint);
    }

    @Test
    @Order(700)
    void testHandler() throws Exception {
        Handler handler = new Handler(6, creator, "handler", ImplementationScope.PRIVATE, configuration, messageType);
        performPost(handler);
    }

    @Test
    @Order(800)
    void testInteractionStarter() throws Exception {
        InteractionStarter interactionStarter = new InteractionStarter(7, creator,
                "interactionStarter", ImplementationScope.PRIVATE, configuration);
        performPost(interactionStarter);
    }

    @Test
    @Order(900)
    void testPersistenceManager() throws Exception {
        PersistenceManager persistenceManager = new PersistenceManager(8, creator,
                "persistenceManager", ImplementationScope.PRIVATE, configuration);
        performPost(persistenceManager);
    }

    @Test
    @Order(1000)
    void testSerializer() throws Exception {
        Serializer serializer = new Serializer(9, creator,
                "serializer", ImplementationScope.PRIVATE, configuration, messageType, protocolType);
        performPost(serializer);
    }
}
