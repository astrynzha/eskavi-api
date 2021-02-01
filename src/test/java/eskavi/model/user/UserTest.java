package eskavi.model.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Niv Adam
 * @version 1.0.0
 */
class UserTest {
    private final static String EMAIL = "email@example.com";
    private final static String HASHED_PASSWORT = "noweshogwg389n";
    private final static String SECURITY_ANSWER = "Mueller";
    private User user;

    @BeforeEach
    void setup() {
        this.user = new User(UserTest.EMAIL, UserTest.HASHED_PASSWORT, UserLevel.BASIC_USER,
                SecurityQuestion.MAIDEN_NAME, UserTest.SECURITY_ANSWER);
    }

    @Test
    void testUserLevel() {
        assertEquals(UserLevel.BASIC_USER, this.user.getUserLevel());
        user.setUserLevel(UserLevel.PUBLISHING_USER);
        assertEquals(UserLevel.PUBLISHING_USER, this.user.getUserLevel());
        user.setUserLevel(UserLevel.ADMINISTRATOR);
        assertEquals(UserLevel.ADMINISTRATOR, this.user.getUserLevel());
        user.setUserLevel(UserLevel.BASIC_USER);
        assertEquals(UserLevel.BASIC_USER, this.user.getUserLevel());
    }

    @Test
    void testGetEmailAddress() {
        assertEquals(UserTest.EMAIL, this.user.getEmailAddress());
    }

    @Test
    void testPassword() {
        final String pw = "test";
        assertEquals(UserTest.HASHED_PASSWORT, this.user.getPassword());
        // set new password
        this.user.setPassword(pw);
        // test new password
        assertEquals(pw, this.user.getPassword());
    }

    @Test
    void testGetSecurityQuestion() {
        assertEquals(SecurityQuestion.MAIDEN_NAME, this.user.getSecurityQuestion());
    }

    @Test
    void testGetSecurityAnswer() {
        assertEquals(UserTest.SECURITY_ANSWER, this.user.getSecurityAnswer());
    }

    /*
    @Test
    void testSubscribe() {
        Implementation imp = new ImplementationStub(1, this.user, "ImpStub", new Scope(ImplementationScope.SHARED));
        // test subscribe
        this.user.subscribe(imp);
        //test isSubscribed to
        assertTrue(this.user.isSubscribedTo(imp));
        //test getSubscribed
        assertTrue(this.user.getSubscribed().contains(imp));
        //test unsubscribe
        this.user.unsubscribe(imp);
        assertFalse(this.user.isSubscribedTo(imp));
        assertFalse(this.user.getSubscribed().contains(imp));

    }*/

}