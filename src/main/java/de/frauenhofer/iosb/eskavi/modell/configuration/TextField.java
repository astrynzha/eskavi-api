package de.frauenhofer.iosb.eskavi.modell.configuration;

public class TextField extends SingleValueField {

    private DataType dataType;

    public TextField(String name, boolean allowMultiple, KeyExpression expression, DataType dataType) {
        super(name, allowMultiple, expression);
        this.dataType = dataType;
    }

    public DataType getDataType() {
        return dataType;
    }

    @Override
    public String resolveKeyExpression() {
        return getKeyExpression().getExpressionStart() + getValue() + getKeyExpression().getExpressionEnd();
    }

    @Override
    public TextField clone() {
        KeyExpression copy = new KeyExpression(getKeyExpression().getExpressionStart(), getKeyExpression().getExpressionEnd());
        return new TextField(getName(), allowsMultiple(), copy, getDataType());
    }
}
