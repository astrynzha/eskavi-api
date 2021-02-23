package eskavi.scenarios;


import com.jayway.jsonpath.JsonPath;
import eskavi.EskaviApplication;
import eskavi.model.configuration.Configuration;
import eskavi.model.configuration.DataType;
import eskavi.model.configuration.KeyExpression;
import eskavi.model.configuration.TextField;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
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
    private MockMvc mvc;
    @Autowired
    UserRepository userRepository;
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
        configuration = new TextField("text", false, new KeyExpression("<text>", "<text>"), DataType.TEXT);
    }

    private String getImp(long moduleId) throws Exception {
        return mvc.perform(get("/api/imp")
                .header("Authorization", "Bearer " + token)
                .queryParam("moduleId", String.valueOf(moduleId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();
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
