package eskavi.scenarios;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import eskavi.EskaviApplication;
import eskavi.controller.requests.imp.AddUserRequest;
import eskavi.controller.requests.imp.RemoveUserRequest;
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
import org.junit.jupiter.api.Assertions;
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

import java.util.List;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    @Autowired
    ImplementationRepository impRepository;
    //dummyData
    User creator;
    User newUser;
    String token;
    //Imps
    Configuration dummy = new TextField("text", false, new KeyExpression("<text>", "<text>"), DataType.TEXT);
    MessageType messageType;
    ProtocolType protocolType;

    private void login() throws Exception {
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
    }

    @BeforeEach
    public void before() throws Exception {
        if (token == null) {
            login();
        }
    }

    private void performPut(ImmutableImplementation implementation) throws Exception {
        String body = new ObjectMapper().writeValueAsString(implementation);
        mvc.perform(put("/api/imp")
                .header("Authorization", "Bearer " + token)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(100)
    void testMessageType() throws Exception {
        messageType = new MessageType(1, creator, "defaultMessageType", ImplementationScope.PRIVATE);
        messageType = impRepository.save(messageType);
        MessageType messageType2 = new MessageType(messageType.getImplementationId(), creator, "messageType", ImplementationScope.SHARED);
        performPut(messageType2);
        Assertions.assertEquals(messageType2, impRepository.findById(messageType.getImplementationId()).get());
    }

    @Test
    @Order(200)
    void testProtocolType() throws Exception {
        protocolType = new ProtocolType(2, creator, "defaultProtocolType", ImplementationScope.PRIVATE);
        protocolType = impRepository.save(protocolType);
        ProtocolType protocolType2 = new ProtocolType(protocolType.getImplementationId(), creator, "protocolType", ImplementationScope.SHARED);
        performPut(protocolType2);
        Assertions.assertEquals(protocolType2, impRepository.findById(protocolType.getImplementationId()).get());
    }

    @Test
    @Order(300)
    void testAssetConnection() throws Exception {
        AssetConnection assetConnection = new AssetConnection(3, creator, "defaultAssetConnection", ImplementationScope.PRIVATE, dummy);
        assetConnection = impRepository.save(assetConnection);
        AssetConnection assetConnection2 = new AssetConnection(assetConnection.getImplementationId(), creator, "assetConnection", ImplementationScope.SHARED, dummy);
        performPut(assetConnection2);
        Assertions.assertEquals(assetConnection2, impRepository.findById(assetConnection.getImplementationId()).get());
    }

    @Test
    @Order(400)
    void testDeserializer() throws Exception {
        Deserializer deserializer = new Deserializer(6, creator, "defaultDeserializer", ImplementationScope.PRIVATE, dummy, messageType, protocolType);
        deserializer = impRepository.save(deserializer);
        Deserializer deserializer2 = new Deserializer(deserializer.getImplementationId(), creator, "deserializer", ImplementationScope.SHARED, dummy, messageType, protocolType);
        performPut(deserializer2);
        Assertions.assertEquals(deserializer2, impRepository.findById(deserializer.getImplementationId()).get());
    }

    @Test
    @Order(500)
    void testDispatcher() throws Exception {
        Dispatcher dispatcher = new Dispatcher(7, creator, "defaultDispatcher", ImplementationScope.PRIVATE, dummy, messageType);
        dispatcher = impRepository.save(dispatcher);
        Dispatcher dispatcher2 = new Dispatcher(dispatcher.getImplementationId(), creator, "dispatcher", ImplementationScope.SHARED, dummy, messageType);
        performPut(dispatcher2);
        Assertions.assertEquals(dispatcher2, impRepository.findById(dispatcher.getImplementationId()).get());
    }

    @Test
    @Order(600)
    void testEndpoint() throws Exception {
        Endpoint endpoint = new Endpoint(8, creator, "defaultEndpoint", ImplementationScope.PRIVATE, dummy, protocolType);
        endpoint = impRepository.save(endpoint);
        Endpoint endpoint2 = new Endpoint(endpoint.getImplementationId(), creator, "endpoint", ImplementationScope.SHARED, dummy, protocolType);
        performPut(endpoint2);
        Assertions.assertEquals(endpoint2, impRepository.findById(endpoint.getImplementationId()).get());
    }

    @Test
    @Order(700)
    void testHandler() throws Exception {
        Handler handler = new Handler(10, creator, "defaultHandler", ImplementationScope.PRIVATE, dummy, messageType);
        handler = impRepository.save(handler);
        Handler handler2 = new Handler(handler.getImplementationId(), creator, "handler", ImplementationScope.SHARED, dummy, messageType);
        performPut(handler2);
        Assertions.assertEquals(handler2, impRepository.findById(handler.getImplementationId()).get());
    }

    @Test
    @Order(800)
    void testInteractionStarter() throws Exception {
        InteractionStarter interactionStarter = new InteractionStarter(4, creator, "defaultInteractionStarter", ImplementationScope.PRIVATE, dummy);
        interactionStarter = impRepository.save(interactionStarter);
        InteractionStarter interactionStarter2 = new InteractionStarter(interactionStarter.getImplementationId(), creator,
                "interactionStarter", ImplementationScope.SHARED, dummy);
        performPut(interactionStarter2);
        Assertions.assertEquals(interactionStarter2, impRepository.findById(interactionStarter.getImplementationId()).get());
    }

    @Test
    @Order(900)
    void testPersistenceManager() throws Exception {
        PersistenceManager persistenceManager = new PersistenceManager(5, creator, "defaultPersistenceManager", ImplementationScope.PRIVATE, dummy);
        persistenceManager = impRepository.save(persistenceManager);
        PersistenceManager persistenceManager2 = new PersistenceManager(persistenceManager.getImplementationId(), creator,
                "persistenceManager", ImplementationScope.SHARED, dummy);
        performPut(persistenceManager2);
        Assertions.assertEquals(persistenceManager2, impRepository.findById(persistenceManager.getImplementationId()).get());
    }

    @Test
    @Order(1000)
    void testSerializer() throws Exception {
        Serializer serializer = new Serializer(9, creator, "defaultSerializer", ImplementationScope.PRIVATE, dummy, messageType, protocolType);
        serializer = impRepository.save(serializer);
        Serializer serializer2 = new Serializer(serializer.getImplementationId(), creator,
                "serializer", ImplementationScope.SHARED, dummy, messageType, protocolType);
        performPut(serializer2);
        Assertions.assertEquals(serializer2, impRepository.findById(serializer.getImplementationId()).get());
    }


    @Test
    @Order(1100)
    void testAddUser() throws Exception {
        AddUserRequest request = new AddUserRequest();
        newUser = new User("abc@gmail.com", new BCryptPasswordEncoder().encode("1234"),
                UserLevel.PUBLISHING_USER, SecurityQuestion.MAIDEN_NAME, "Julia");
        userRepository.save(newUser);
        request.setUserIds(List.of(newUser.getEmailAddress()));
        request.setImpId(messageType.getImplementationId());
        String body = new ObjectMapper().writeValueAsString(request);
        mvc.perform(post("/api/imp/user")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());
    }

    @Test
    @Order(1200)
    void testRemoveUser() throws Exception {
        RemoveUserRequest request = new RemoveUserRequest();
        request.setUserId(newUser.getEmailAddress());
        request.setImpId(messageType.getImplementationId());
        String body = new ObjectMapper().writeValueAsString(request);
        mvc.perform(delete("/api/imp/user")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());
    }
}
