package eskavi.scenarios;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import eskavi.EskaviApplication;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"}, classes = EskaviApplication.class)
@DirtiesContext
@AutoConfigureMockMvc
public class CreateModuleImpFromExisting {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImplementationRepository impRepository;
    //dummyData
    User creator;
    String token;
    //Imps
    Configuration configuration;
    long messageTypeId;
    long protocolTypeId;
    long assetConnectionId;
    long serializerId;
    long deserializerId;
    long dispatcherId;
    long endpointId;
    long handlerId;
    long interactionStarterId;
    long persistenceManagerId;
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() throws Exception {
        //TODO setUp all Imps in Repository
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
        MessageType messageType = new MessageType(1, creator, "defaultMessageType", ImplementationScope.PRIVATE);
        messageTypeId = impRepository.save(messageType).getImplementationId();

        ProtocolType protocolType = new ProtocolType(2, creator, "defaultProtocolType", ImplementationScope.PRIVATE);
        protocolTypeId = impRepository.save(protocolType).getImplementationId();

        AssetConnection assetConnection = new AssetConnection(3, creator, "defaultAssetConnection", ImplementationScope.PRIVATE, configuration);
        assetConnectionId = impRepository.save(assetConnection).getImplementationId();

        InteractionStarter interactionStarter = new InteractionStarter(4, creator, "defaultInteractionStarter", ImplementationScope.PRIVATE, configuration);
        interactionStarterId = impRepository.save(interactionStarter).getImplementationId();

        PersistenceManager persistenceManager = new PersistenceManager(5, creator, "defaultPersistenceManager", ImplementationScope.PRIVATE, configuration);
        persistenceManagerId = impRepository.save(persistenceManager).getImplementationId();

        Deserializer deserializer = new Deserializer(6, creator, "defaultDeserializer", ImplementationScope.PRIVATE, configuration, messageType, protocolType);
        deserializerId = impRepository.save(deserializer).getImplementationId();

        Dispatcher dispatcher = new Dispatcher(7, creator, "defaultDispatcher", ImplementationScope.PRIVATE, configuration, messageType);
        dispatcherId = impRepository.save(dispatcher).getImplementationId();

        Endpoint endpoint = new Endpoint(8, creator, "defaultEndpoint", ImplementationScope.PRIVATE, configuration, protocolType);
        endpointId = impRepository.save(endpoint).getImplementationId();

        Serializer serializer = new Serializer(9, creator, "defaultSerializer", ImplementationScope.PRIVATE, configuration, messageType, protocolType);
        serializerId = impRepository.save(serializer).getImplementationId();

        Handler handler = new Handler(10, creator, "defaultHandler", ImplementationScope.PRIVATE, configuration, messageType);
        handlerId = impRepository.save(handler).getImplementationId();
    }

    private String getImp(long moduleId) throws Exception {
        return new ObjectMapper().writeValueAsString(JsonPath.read(mvc.perform(get("/api/imp")
                .header("Authorization", "Bearer " + token)
                .queryParam("moduleId", String.valueOf(moduleId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), "$.implementations[0]"));
    }

    private void addImp(String implementation) throws Exception {
        mvc.perform(post("/api/imp")
                .header("Authorization", "Bearer " + token)
                .content(implementation)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateMessageTypeFromExisting() throws Exception {
        addImp(getImp(messageTypeId));
    }

    @Test
    void testCreateProtocolTypeFromExisting() throws Exception {
        addImp(getImp(protocolTypeId));
    }

    @Test
    void testCreateAssetConnectionFromExisting() throws Exception {
        addImp(getImp(assetConnectionId));
    }

    @Test
    void testCreateDeserializerFromExisting() throws Exception {
        addImp(getImp(deserializerId));
    }

    @Test
    void testCreateDispatcherFromExisting() throws Exception {
        addImp(getImp(dispatcherId));
    }

    @Test
    void testCreateEndpointFromExisting() throws Exception {
        addImp(getImp(endpointId));
    }

    @Test
    void testCreateInteractionStarterFromExisting() throws Exception {
        addImp(getImp(interactionStarterId));
    }

    @Test
    void testCreatePersistenceManagerFromExisting() throws Exception {
        addImp(getImp(persistenceManagerId));
    }

    @Test
    void testCreateSerializerFromExisting() throws Exception {
        addImp(getImp(serializerId));
    }

    @Test
    void testCreateHandlerFromExisting() throws Exception {
        addImp(getImp(handlerId));
    }
}
