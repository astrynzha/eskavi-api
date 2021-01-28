package de.frauenhofer.iosb.eskavi.model.configuration;

import java.util.Objects;

/**
 * Class extends {@link Configuration} and is an abstraction of all Configurations just containing one value
 */
public abstract class SingleValueField extends Configuration {

    private String value;

    /**
     * Constructs a new SingleValueField
     *
     * @param name          the name of this SingleValueField
     * @param allowMultiple boolean, whether this SingleValueField can be added multiple times
     * @param expression    the {@link KeyExpression} of this SingleValueField
     */
    public SingleValueField(String name, boolean allowMultiple, KeyExpression expression) {
        super(name, allowMultiple, expression);
    }

    /**
     * @return the set value of this SingleValueField or NULL if value not set yet
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of this SingleValueField
     *
     * @param value the new Value of the field
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String resolveKeyExpression() {
        if (getValue() != null) {
            return getKeyExpression().getExpressionStart() + getValue() + getKeyExpression().getExpressionEnd();
        } else {
            throw new IllegalStateException("A value has to be assigned to Configuration: " + getName());
        }
    }

    @Override
    public boolean checkCompatible() {
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getValue());
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
    public String toString() {
        return "SingleValueField{" +
                "name='" + getName() + '\'' +
                ", allowMultiple=" + allowsMultiple() +
                ", keyExpression=" + getKeyExpression().toString() +
                ", value='" + getValue() + "'}";
    }
}
