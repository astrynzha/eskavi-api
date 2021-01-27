package de.frauenhofer.iosb.eskavi.model.configuration;

/**
 * Represents the invariant parts of the KeyExpression of a Configuration. Is a simple storage class.
 */
public class KeyExpression {
    private String expressionStart;
    private String expressionEnd;

    /**
     * Constructs a new KeyExpression
     * @param expressionStart
     * @param expressionEnd
     */
    public KeyExpression(String expressionStart, String expressionEnd) {
        setExpressionStart(expressionStart);
        setExpressionEnd(expressionEnd);
    }

    /**
     * Returns the part of the KeyExpression to be added in front of the value
     * @return expressionStart
     */
    public String getExpressionStart() {
        return expressionStart;
    }

    /**
     * Sets the beginning of the KeyExpression
     * @param expressionStart String to start the expression
     */
    public void setExpressionStart(String expressionStart) {
        this.expressionStart = expressionStart;
    }

    /**
     * Returns the part of the KeyExpression to be added after the value
     * @return expressionEnd
     */
    public String getExpressionEnd() {
        return expressionEnd;
    }

    /**
     * Sets the beginning of the KeyExpression
     * @param expressionEnd String to start the expression
     */
    public void setExpressionEnd(String expressionEnd) {
        this.expressionEnd = expressionEnd;
    }
}
