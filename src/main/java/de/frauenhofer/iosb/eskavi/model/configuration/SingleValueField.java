package de.frauenhofer.iosb.eskavi.model.configuration;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "SingleValueField{" +
                "name='" + getName() + '\'' +
                ", allowMultiple=" + allowsMultiple() +
                ", keyExpression=" + getKeyExpression().toString() +
                ", value='" + getValue() + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SingleValueField that = (SingleValueField) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getValue());
    }
}
