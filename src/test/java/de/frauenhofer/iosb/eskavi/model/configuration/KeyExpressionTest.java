package de.frauenhofer.iosb.eskavi.model.configuration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyExpressionTest {

    private KeyExpression testObject;

    @BeforeEach
    void setUp() {
        testObject = new KeyExpression("testStart", "testEnd");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getExpressionStart() {
        assertEquals("testStart", testObject.getExpressionStart());
    }

    @Test
    void setExpressionStart() {
        testObject.setExpressionStart("newTestStart");
        assertEquals("newTestStart", testObject.getExpressionStart());
    }

    @Test
    void getExpressionEnd() {
        assertEquals("testEnd", testObject.getExpressionEnd());
    }

    @Test
    void setExpressionEnd() {
        testObject.setExpressionEnd("newTestEnd");
        assertEquals("newTestEnd", testObject.getExpressionEnd());
    }
}