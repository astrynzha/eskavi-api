package de.frauenhofer.iosb.eskavi.model.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SelectTest {
    private Select testObject;
    private Map<String, String> content;

    @BeforeEach
    void setUp() {
        content = new HashMap<>();
        content.put("test", "testValue");
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
        testObject.setValue("test");
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
        assertNotEquals(testObject.getKeyExpression(), clone.getKeyExpression());

        //changing clone
        Map<String, String> newContent = new HashMap<>();
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
        Map<String, String> newContent = new HashMap<>();
        newContent.put("fail", "fail");
        Select other = new Select("test", false, new KeyExpression("start", "end"), newContent);
        assertEquals(false, testObject.equals(other));
    }

    @Test
    void testHashCode() {
        TextField other = new TextField("test", false, new KeyExpression("test", "."), DataType.TEXT);
        assertEquals(testObject.hashCode(), other.hashCode());
    }
}