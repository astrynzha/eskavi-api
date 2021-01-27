package de.frauenhofer.iosb.eskavi.model.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextFieldTest {
    private TextField testObject;

    @BeforeEach
    void setUp(){
        testObject = new TextField("test", false, new KeyExpression("test", "."), DataType.TEXT);
    }

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
}