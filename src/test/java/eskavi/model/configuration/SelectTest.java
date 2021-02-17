package eskavi.model.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SelectTest {
    private Select testObject;
    private List<String> content;

    @BeforeEach
    void setUp() {
        content = new ArrayList<>();
        content.add("testValue");
        this.testObject = new Select("test", false, new KeyExpression("start", "end"), content);
    }

    @Test
    void getContent() {
        assertEquals(content, testObject.getContent());
    }

    @Test
    void setContent() {
    }

    @Test
    void setValueSuccess() {
        testObject.setValue("testValue");
        assertEquals("testValue", testObject.getValue());
    }

    @Test
    void setValueFailure() {
        try {
            testObject.setValue("no key");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(true, true);
        }
    }

    @Test
    void testClone() {
        Select clone = testObject.clone();
        assertEquals(testObject.getContent(), clone.getContent());
        assertEquals(testObject.getKeyExpression().getExpressionStart(), clone.getKeyExpression().getExpressionStart());
        assertEquals(testObject.getKeyExpression(), clone.getKeyExpression());

        //changing clone
        List<String> newContent = new ArrayList<>();
        clone.setContent(newContent);
        assertNotEquals(testObject.getContent(), clone.getContent());
    }

    @Test
    void testEqualsSuccess() {
        Select other = new Select("test", false, new KeyExpression("start", "end"), content);
        assertEquals(true, testObject.equals(other));
    }

    @Test
    void testEqualsFailure() {
        List<String> newContent = new ArrayList<>();
        newContent.add("fail");
        Select other = new Select("test", false, new KeyExpression("start", "end"), newContent);
        assertEquals(false, testObject.equals(other));
    }
}