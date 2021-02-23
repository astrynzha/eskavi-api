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
import eskavi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"}, classes = EskaviApplication.class)
@DirtiesContext
@AutoConfigureMockMvc
public class EditModuleImp {

    //SpringBootComponents
    @Autowired
    private MockMvc mvc;
    @Autowired
    UserRepository userRepository;
    //dummyData
    User creator;
    String token;
    //Imps
    Configuration configuration;
    MessageType messageType;
    ProtocolType protocolType;
    AssetConnection assetConnection;
    Serializer serializer;
    Deserializer deserializer;
    Dispatcher dispatcher;
    Endpoint endpoint;
    Handler handler;
    InteractionStarter interactionStarter;
    PersistenceManager persistenceManager;

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
        configuration = new TextField("text", false, new KeyExpression("<text>", "<text>"), DataType.TEXT);
    }

    private void performPut(ImmutableImplementation implementation) throws Exception {
        String body = new ObjectMapper().writeValueAsString(implementation);
        mvc.perform(put("/api/imp")
                .header("Authorization", "Bearer " + token)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(100)
    void testMessageType() throws Exception {
        messageType = new MessageType(0, creator, "messageType", ImplementationScope.SHARED);
        performPut(messageType);
    }

    @Test
    @Order(200)
    void testProtocolType() throws Exception {
        protocolType = new ProtocolType(1, creator, "protocolType", ImplementationScope.SHARED);
        performPut(protocolType);
    }

    @Test
    @Order(300)
    void testAssetConnection() throws Exception {
        assetConnection = new AssetConnection(2, creator, "assetConnection", ImplementationScope.SHARED, configuration);
        performPut(assetConnection);
    }

    @Test
    @Order(400)
    void testDeserializer() throws Exception {
        deserializer = new Deserializer(3, creator, "deserializer", ImplementationScope.SHARED, configuration, messageType, protocolType);
        performPut(deserializer);
    }

    @Test
    @Order(500)
    void testDispatcher() throws Exception {
        dispatcher = new Dispatcher(4, creator, "dispatcher", ImplementationScope.SHARED, configuration, messageType);
        performPut(dispatcher);
    }

    @Test
    @Order(600)
    void testEndpoint() throws Exception {
        endpoint = new Endpoint(5, creator, "endpoint", ImplementationScope.SHARED, configuration, protocolType);
        performPut(endpoint);
    }

    @Test
    @Order(700)
    void testHandler() throws Exception {
        handler = new Handler(6, creator, "handler", ImplementationScope.SHARED, configuration, messageType);
        performPut(handler);
    }

    @Test
    @Order(800)
    void testInteractionStarter() throws Exception {
        interactionStarter = new InteractionStarter(7, creator,
                "interactionStarter", ImplementationScope.SHARED, configuration);
        performPut(interactionStarter);
    }

    @Test
    @Order(900)
    void testPersistenceManager() throws Exception {
        persistenceManager = new PersistenceManager(8, creator,
                "persistenceManager", ImplementationScope.SHARED, configuration);
        performPut(persistenceManager);
    }

    @Test
    @Order(1000)
    void testSerializer() throws Exception {
        serializer = new Serializer(9, creator,
                "serializer", ImplementationScope.SHARED, configuration, messageType, protocolType);
        performPut(serializer);
    }
}
