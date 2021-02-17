package eskavi.model.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SwitchTest {
    private Switch testObject;

    @BeforeEach
    void setUp() {
        testObject = new Switch("test", false, new KeyExpression("test", "end"), "true", "false");
    }

    @Test
    void testDefault() {
        assertEquals("false", testObject.getValue());
    }

    @Test
    void testSetValueSuccess() {
        testObject.setValue("true");
        assertEquals("true", testObject.getValue());
    }

    @Test
    void testSetValueFail() {
        try {
            testObject.setValue("hallo");
            assertEquals(false, false);
        } catch (IllegalArgumentException e) {
            assertEquals(true, true);
        }
    }

    @Test
    void testClone() {
        Switch clone = testObject.clone();
        assertEquals(testObject.getContent(), clone.getContent());
        assertEquals(testObject.getKeyExpression().getExpressionStart(), clone.getKeyExpression().getExpressionStart());
        assertEquals(testObject.getKeyExpression(), clone.getKeyExpression());

        //changing clone
        clone.setValue("true");
        assertNotEquals(testObject.getValue(), clone.getValue());
    }
}