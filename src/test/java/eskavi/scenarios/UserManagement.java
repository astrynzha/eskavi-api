package eskavi.scenarios;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import eskavi.EskaviApplication;
import eskavi.controller.requests.user.ResetPasswordRequest;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
import eskavi.repository.ImplementationRepository;
import eskavi.repository.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;

@SpringBootTest(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"}, classes = EskaviApplication.class)
@DirtiesContext
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserManagement {
    @Autowired
    private MockMvc mvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ImplementationRepository implementationRepository;

    User creator;

    @Test
    @Order(100)
    void testRegister() throws Exception {
        creator = new User("a@gmail.com", new BCryptPasswordEncoder().encode("1234"),
                UserLevel.PUBLISHING_USER, SecurityQuestion.MAIDEN_NAME, "Julia");
        MvcResult result = mvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"email\":\"a@gmail.com\",\n" +
                        "\t\"password\":\"1234\"\n" +
                        "}"))
                .andReturn();
        String token = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");
        //TODO How to test if token is valid? with user token matcher?!
    }

    @Test
    @Order(200)
    void testLogin() throws Exception {
        MvcResult result = mvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"email\":\"a@gmail.com\",\n" +
                        "\t\"password\":\"1234\"\n" +
                        "}"))
                .andReturn();
        String token = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");
    }

    @Test
    @Order(300)
    void testReset() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setAnswer("");
        request.setNewPassword("");
        MvcResult result = mvc.perform(post("/api/user/reset_password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andReturn();
    }

    @Test
    @Order(400)
    void testSetNewPassword() {

    }

    @Test
    @Order(400)
    void testResetPassword() {

    }
}
