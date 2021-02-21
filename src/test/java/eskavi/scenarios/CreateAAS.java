package eskavi.scenarios;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import eskavi.EskaviApplication;
import eskavi.controller.requests.aas.AddModuleImpRequest;
import eskavi.model.implementation.moduleimp.AssetConnection;
import eskavi.model.implementation.moduleimp.Endpoint;
import eskavi.model.implementation.moduleimp.Handler;
import eskavi.model.implementation.moduleimp.PersistenceManager;
import eskavi.repository.ImplementationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
public class CreateAAS {
    @Autowired
    private MockMvc mvc;
    @Autowired
    ImplementationRepository implementationRepository;

    AssetConnection assetConnection;
    Handler handlerA;
    Handler handlerB;
    PersistenceManager persistenceManager;
    Endpoint endpoint;

    @BeforeEach
    void setUp() throws Exception {
        //prepare imps to add
    }

    @Test
    void createAASFromScenario() throws Exception {
        //create Session
        MvcResult result = mvc.perform(post("/api/aas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        long sessionId = Long.parseLong(JsonPath.read(result.getResponse().getContentAsString(), "$.sessionId"));
        //add ModuleImps
        addModuleImpToAAS(sessionId, assetConnection.getImplementationId());
        addModuleImpToAAS(sessionId, handlerA.getImplementationId());
        addModuleImpToAAS(sessionId, handlerB.getImplementationId());
        addModuleImpToAAS(sessionId, persistenceManager.getImplementationId());
        addModuleImpToAAS(sessionId, endpoint.getImplementationId());
        //updateConfigurations
        //generate java class
        mvc.perform(get("/api/aas/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("sessionId", String.valueOf(sessionId))
        );
    }

    private void addModuleImpToAAS(long sessionId, long moduleId) throws Exception {
        AddModuleImpRequest impRequest = new AddModuleImpRequest();
        impRequest.setSessionId(sessionId);
        impRequest.setImpId(moduleId);
        String body = new ObjectMapper().writeValueAsString(impRequest);
        mvc.perform(post("/api/aas/imp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }
}
