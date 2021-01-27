package de.frauenhofer.iosb.eskavi.model.configuration;

public abstract class SingleValueField extends Configuration {

    private String value;

    public SingleValueField(String name, boolean allowMultiple, KeyExpression expression) {
        super(name, allowMultiple, expression);
    }

    @Override
    public boolean checkCompatible() {
        return true;
    }

    @Override
    public String resolveKeyExpression() {
        if (getValue() != null) {
            return getKeyExpression().getExpressionStart() + getValue() + getKeyExpression().getExpressionEnd();
        } else {
            throw new IllegalStateException("A value has to be assigned to Configuration: " + getName());
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
