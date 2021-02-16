package eskavi.service;

import eskavi.model.user.SecurityQuestion;
import eskavi.repository.UserRepository;
import eskavi.service.mockrepo.MockUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;


import static org.junit.jupiter.api.Assertions.*;

class UserManagementServiceTest {

    @Autowired
    UserRepository userRepository;
    private UserManagementService userService;
    private String someEmail1;
    private String someEmail2;
//    private ImmutableUser userA;
//    private ImmutableUser userB;
//    private ImmutableUser userC;

    @BeforeEach
    void setUp() {
        someEmail1 = "a.str@gmail.com";
        someEmail2 = "a.str@gmail.com";
        userService = new UserManagementService(new MockUserRepository());
//        userService = new UserManagementService(userRepository);
//        userA = userService.createUser("a.str@gmail.com", "dja;lsfkdjsafk");
//        userB = userService.createUser("str@gmail.com", "dsa;lfj[b");
//        userC = userService.createUser("str1@gmail.com", "dsa;lfj[b");
    }

    @Test
    void createUser() {
        userService.createUser(someEmail1, "askfjapojwe");
        assertEquals(someEmail1, userService.getUser(someEmail1).getEmailAddress());
        Exception exception = assertThrows(ResponseStatusException.class,
                () -> userService.createUser(someEmail1, "djkafsk"));
        assertTrue(exception.getMessage().contains("email is already registered"));
    }

    @Test
    void getUser() {
        // at start is the repo empty
        assertThrows(ResponseStatusException.class,
                () -> userService.getUser("a.str@gmail.com"));
    }

    // TODO when do we create ADMIN users
    @Test
    void setUserLevel() {
        userService.createUser(someEmail1, "klda;sfj");
        userService.createUser(someEmail2, "ds;afj");
    }

    @Test
    void deleteUser() {
        userService.createUser(someEmail1, "askfjapojwe");
        assertEquals(someEmail1, userService.getUser(someEmail1).getEmailAddress());
        userService.deleteUser(someEmail1);
        assertThrows(ResponseStatusException.class, () ->
                userService.getUser(someEmail1));
    }

    @Test
    void setPassword() {
        userService.createUser(someEmail1, "a");
        assertTrue(userService.checkPassword(someEmail1, "a"));
        userService.setPassword(someEmail1, "b");
        assertTrue(userService.checkPassword(someEmail1, "b"));
    }

    // TODO: check with other questions
    @Test
    void getSecurityQuestion() {
        userService.createUser(someEmail1, "a");
        assertEquals(SecurityQuestion.MAIDEN_NAME, userService.getSecurityQuestion(someEmail1));
    }

    @Test
    void checkSecurityQuestion() {
        // TODO
    }

    @Test
    void checkPassword() {
        userService.createUser(someEmail1, "dk;jafq[w3");
        assertTrue(userService.checkPassword(someEmail1, "dk;jafq[w3"));
    }
}