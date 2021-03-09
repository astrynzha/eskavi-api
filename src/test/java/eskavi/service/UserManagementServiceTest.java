package eskavi.service;

import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
import eskavi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import org.springframework.web.server.ResponseStatusException;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserManagementServiceTest {

    @Autowired
    private UserRepository repository;

    private UserManagementService userService;
    private String someEmail1;
    private String someEmail2;
//    private ImmutableUser userA;
//    private ImmutableUser userB;
//    private ImmutableUser userC;

    @BeforeEach
    void setUp() {
        someEmail1 = "a.str@gmail.com";
        someEmail2 = "str@gmail.com";
        userService = new UserManagementService(repository);
//        userA = userService.createUser("a.str@gmail.com", "dja;lsfkdjsafk");
//        userB = userService.createUser("str@gmail.com", "dsa;lfj[b");
//        userC = userService.createUser("str1@gmail.com", "dsa;lfj[b");
    }

    @Test
    void createUser() {
        userService.createUser(someEmail1, "askfjapojwe", SecurityQuestion.MAIDEN_NAME, "Jj");
        assertEquals(someEmail1, userService.getUser(someEmail1).getEmailAddress());
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> userService.createUser(someEmail1, "djkafsk",
                        SecurityQuestion.MAIDEN_NAME, "Kk"));
        assertTrue(exception.getMessage().contains("email is already registered"));
    }

    @Test
    void getUser() {
        // at start is the repo empty
        assertThrows(ResponseStatusException.class,
                () -> userService.getUser("a.str@gmail.com"));
    }

    @Test
    void setUserLevel() {
        User admin = new User("a@gmail.com", "d;afjsdkjf", UserLevel.ADMINISTRATOR,
                SecurityQuestion.MAIDEN_NAME, "x");
        repository.save(admin);
        userService.createUser(someEmail1, "klda;sfj",
                SecurityQuestion.MAIDEN_NAME, "Bezos");
        Optional<User> ou = repository.findById(someEmail1);
        if (ou.isEmpty()) {
            fail();
        }
        User u = ou.get();
        assertEquals(u.getUserLevel(), UserLevel.BASIC_USER);
        userService.setUserLevel(someEmail1, UserLevel.ADMINISTRATOR, "a@gmail.com");
        ou = repository.findById(someEmail1);
        if (ou.isEmpty()) {
            fail();
        }
        u = ou.get();
        assertEquals(u.getUserLevel(), UserLevel.ADMINISTRATOR);
    }

    @Test
    void checkSecurityQuestion() {
        User admin = new User("a@gmail.com", "d;afjsdkjf", UserLevel.ADMINISTRATOR,
                SecurityQuestion.MAIDEN_NAME, "x");
        repository.save(admin);
        assertTrue(userService.checkSecurityQuestion("a@gmail.com", "x"));
    }

    @Test
    void deleteUser() {
        userService.createUser(someEmail1, "askfjapojwe",
                SecurityQuestion.MAIDEN_NAME, "Bezos");
        assertEquals(someEmail1, userService.getUser(someEmail1).getEmailAddress());
        userService.deleteUser(someEmail1);
        assertThrows(ResponseStatusException.class, () ->
                userService.getUser(someEmail1));
    }

    @Test
    void setPassword() {
        userService.createUser(someEmail1, "a",
                SecurityQuestion.MAIDEN_NAME, "Bezos");
        assertTrue(userService.checkPassword(someEmail1, "a"));
        userService.setPassword(someEmail1, "b");
        assertTrue(userService.checkPassword(someEmail1, "b"));
    }

    // TODO: check with other questions
    @Test
    void getSecurityQuestion() {
        userService.createUser(someEmail1, "a",
                SecurityQuestion.MAIDEN_NAME, "Bezos");
        assertEquals(SecurityQuestion.MAIDEN_NAME, userService.getSecurityQuestion(someEmail1));
    }

    @Test
    void checkPassword() {
        userService.createUser(someEmail1, "dk;jafq[w3",
                SecurityQuestion.MAIDEN_NAME, "Bezos");
        assertTrue(userService.checkPassword(someEmail1, "dk;jafq[w3"));
    }
}