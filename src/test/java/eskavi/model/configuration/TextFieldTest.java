package eskavi.model.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextFieldTest {
    private TextField testObject;

    @Test
    void getDataType() {
        assertEquals(DataType.TEXT, testObject.getDataType());
    }

    @Test
    void testClone() {
        TextField clone = testObject.clone();
        //assert whether clone is equal
        assertEquals(testObject.getName(), clone.getName());
        assertEquals(testObject.allowsMultiple(), clone.allowsMultiple());
        assertEquals(testObject.getKeyExpression().getExpressionStart(), clone.getKeyExpression().getExpressionStart());
        assertEquals(testObject.getValue(), clone.getValue());
        clone.setValue("test");
        //assert whether changes to clone dont happen to testObject
        assertNotEquals(testObject.getValue(), clone.getValue());
    }

    @Test
    void testEqualsItself() {
        assertTrue(testObject.equals(testObject));
    }

    @Test
    void testNullNotEqual() {
        assertFalse(testObject.equals(null));
    }

    @Test
    void testEqualsClone() {
        assertTrue(testObject.equals(testObject.clone()));
    }

    @Test
    void testEqualsSuccess() {
        TextField other = new TextField("test", false, new KeyExpression("test", "."), DataType.TEXT);
        assertTrue(testObject.equals(other));
    }

    @Test
    void testEqualsFail() {
        TextField other = new TextField("fail", false, new KeyExpression("test", "."), DataType.TEXT);
        assertFalse(testObject.equals(other));

        other = new TextField("test", true, new KeyExpression("test", "."), DataType.TEXT);
        assertFalse(testObject.equals(other));

        other = new TextField("test", false, new KeyExpression(".", "."), DataType.TEXT);
        assertFalse(testObject.equals(other));

        other = new TextField("test", false, new KeyExpression("test", "."), DataType.DATE);
        assertFalse(testObject.equals(other));
    }

    @Test
    void testHashCode() {
        TextField other = new TextField("test", false, new KeyExpression("test", "."), DataType.TEXT);
        assertEquals(testObject.hashCode(), other.hashCode());
    }

    @BeforeEach
    void setUp() {
        testObject = new TextField("test", false, new KeyExpression("test", "."), DataType.TEXT);
    }
}