package eskavi.model.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Niv Adam
 * @version 1.0.0
 */
class UserTest {
    private User user;

    @BeforeEach
    void setup() {
        this.user = new User("email@example.com", "noweshogwg389n", UserLevel.BASIC_USER, SecurityQuestion.MAIDEN_NAME, "Mueller");
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

}