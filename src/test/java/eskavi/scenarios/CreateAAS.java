package eskavi.scenarios;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import eskavi.EskaviApplication;
import eskavi.controller.requests.aas.AddModuleImpRequest;
import eskavi.controller.requests.aas.UpdateConfigurationRequest;
import eskavi.model.configuration.Configuration;
import eskavi.model.configuration.DataType;
import eskavi.model.configuration.KeyExpression;
import eskavi.model.configuration.TextField;
import eskavi.model.implementation.Implementation;
import eskavi.model.implementation.ImplementationScope;
import eskavi.model.implementation.MessageType;
import eskavi.model.implementation.ProtocolType;
import eskavi.model.implementation.moduleimp.AssetConnection;
import eskavi.model.implementation.moduleimp.Endpoint;
import eskavi.model.implementation.moduleimp.Handler;
import eskavi.model.implementation.moduleimp.PersistenceManager;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
import eskavi.repository.ImplementationRepository;
import eskavi.repository.UserRepository;
import eskavi.util.ImpCreatorUtil;
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

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"}, classes = EskaviApplication.class)
@DirtiesContext
@AutoConfigureMockMvc
public class CreateAAS {
    @Autowired
    private MockMvc mvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImplementationRepository implementationRepository;

    User creator;
    String token;
    ProtocolType protocolType;
    MessageType messageType;
    ImpCreatorUtil impCreatorUtil = new ImpCreatorUtil();
    Implementation handler;
    Implementation assetConnection;
    Implementation persistenceManager;
    Implementation endpoint;


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
        protocolType = implementationRepository.save(impCreatorUtil.getProtocolTypeA());
        messageType = implementationRepository.save(impCreatorUtil.getMessageTypeA());
        assetConnection = assetConnection();
        handler = handler();
        persistenceManager = persistenceManager();
        endpoint = endpoint();
    }

    @Test
    void createAASFromScenario() throws Exception {
        //create Session
        MvcResult result = mvc.perform(post("/api/aas")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        long sessionId = (int) JsonPath.read(result.getResponse().getContentAsString(), "$.sessionId");
        //add ModuleImps
        addModuleImpToAAS(sessionId, assetConnection);
        addModuleImpToAAS(sessionId, handler);
        addModuleImpToAAS(sessionId, persistenceManager);
        addModuleImpToAAS(sessionId, endpoint);
        //updateConfigurations
        TextField dummy = new TextField("dummy", false, new KeyExpression("new Implementation(\"", "\");"), DataType.TEXT);
        dummy.setValue("");
        updateConfiguration(dummy, assetConnection.getImplementationId(), sessionId);
        updateConfiguration(dummy, handler.getImplementationId(), sessionId);
        updateConfiguration(dummy, persistenceManager.getImplementationId(), sessionId);
        updateConfiguration(dummy, endpoint.getImplementationId(), sessionId);
        //generate java class
        MvcResult result1 = mvc.perform(get("/api/aas/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("sessionId", String.valueOf(sessionId))
        ).andExpect(status().isOk()).andReturn();
    }

    private Implementation endpoint() {
        Endpoint endpoint = new Endpoint(1, creator, "Endpoint", ImplementationScope.SHARED, impCreatorUtil.getDummy(), protocolType);
        return implementationRepository.save(endpoint);
    }

    private Implementation persistenceManager() {
        PersistenceManager persistenceManager = new PersistenceManager(12, creator,
                "PersistanceManager", ImplementationScope.SHARED, impCreatorUtil.getDummy());
        return implementationRepository.save(persistenceManager);
    }

    private Implementation handler() {
        Handler handler = new Handler(10, creator, "Handler", ImplementationScope.SHARED, impCreatorUtil.getDummy(), messageType);
        return implementationRepository.save(handler);
    }

    private Implementation assetConnection() {
        AssetConnection assetConnection = new AssetConnection(6, creator, "Assetconnection", ImplementationScope.PUBLIC, impCreatorUtil.getDummy());
        return implementationRepository.save(assetConnection);
    }

    private void updateConfiguration(Configuration configuration, long impId, long sessionId) throws Exception {
        UpdateConfigurationRequest updateConfigurationRequest = new UpdateConfigurationRequest(configuration, impId, sessionId);
        String body = new ObjectMapper().writeValueAsString(updateConfigurationRequest);
        mvc.perform(put("/api/aas/imp/configuration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    private void addModuleImpToAAS(long sessionId, Implementation implementation) throws Exception {
        AddModuleImpRequest impRequest = new AddModuleImpRequest();
        impRequest.setSessionId(sessionId);
        impRequest.setImpId(implementation.getImplementationId());
        String body = new ObjectMapper().writeValueAsString(impRequest);
        mvc.perform(post("/api/aas/imp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }
}
