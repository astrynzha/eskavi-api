package eskavi.scenarios;

import eskavi.EskaviApplication;
import eskavi.repository.ImplementationRepository;
import eskavi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"}, classes = EskaviApplication.class)
@DirtiesContext
@AutoConfigureMockMvc
public class ImplementationIntegrationTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImplementationRepository impRepository;

    @Test
    void testGetImplementationTypes() throws Exception {
        mvc.perform(get("/api/imp/types")).andExpect(status().isOk());
    }

    @Test
    void testGetConfigDataTypes() throws Exception {
        mvc.perform(get("/api/imp/config/data_types")).andExpect(status().isOk());
    }

    @Test
    void testConfigTemplates() throws Exception {
        mvc.perform(get("/api/imp/configTemplates")).andExpect(status().isOk());
    }

    @Test
    void testGetImpScopes() throws Exception {
        mvc.perform(get("/api/imp/scopes")).andExpect(status().isOk());
    }

    @Test
    void testGetDefaultImpCreate() throws Exception {
        mvc.perform(get("/api/imp/default")
                .param("impType", "ASSET_CONNECTION"))
                .andExpect(status().isOk());
    }
}
