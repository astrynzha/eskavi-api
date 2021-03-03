package eskavi.scenarios;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import eskavi.EskaviApplication;
import eskavi.controller.requests.user.LoginRequest;
import eskavi.controller.requests.user.ResetPasswordRequest;
import eskavi.controller.requests.user.SetPasswordRequest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @Autowired
    BCryptPasswordEncoder encoder;

    User creator;
    String token;

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

    @Test
    @Order(100)
    void testRegister() throws Exception {
        MvcResult result = mvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"email\":\"a@gmail.com\",\n" +
                        "\t\"password\":\"1234\"\n" +
                        "}"))
                .andExpect(status().isOk())
                .andReturn();
        token = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");
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
                .andExpect(status().isOk())
                .andReturn();
        token = JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");
    }

    @Test
    @Order(300)
    void testResetPassword() throws Exception {
        login();
        ResetPasswordRequest request = new ResetPasswordRequest();
        String newPassword = "12345678";
        request.setAnswer("Julia");
        request.setNewPassword(newPassword);
        mvc.perform(post("/api/user/reset_password")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(new ObjectMapper().writeValueAsString(request)));
        mvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new LoginRequest(creator.getEmailAddress(), newPassword))))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(400)
    void testSetNewPassword() throws Exception {
        login();
        SetPasswordRequest request = new SetPasswordRequest();
        String newPassword = "12345678";
        request.setOldPassword("1234");
        request.setNewPassword(newPassword);
        mvc.perform(post("/api/user/change_password")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(new ObjectMapper().writeValueAsString(request)));
        mvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new LoginRequest(creator.getEmailAddress(), newPassword))))
                .andExpect(status().isOk())
                .andReturn();
    }
}
