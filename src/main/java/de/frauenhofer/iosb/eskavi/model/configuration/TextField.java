package de.frauenhofer.iosb.eskavi.model.configuration;

import java.util.Objects;

/**
 * Class extends {@link SingleValueField} and represents a Configuration expecting a free text entry
 */
public class TextField extends SingleValueField {

    private DataType dataType;

    /**
     * Constructs a new TextField
     *
     * @param name          the name of this TextField
     * @param allowMultiple boolean, whether this TextField can be added multiple times
     * @param expression    the {@link KeyExpression} of this TextField
     * @param dataType      the {@link DataType} of this TextField
     */
    public TextField(String name, boolean allowMultiple, KeyExpression expression, DataType dataType) {
        super(name, allowMultiple, expression);
        this.dataType = dataType;
    }

    /**
     * @return the expected {@link DataType} of this TextField.
     */
    public DataType getDataType() {
        return dataType;
    }

    /**
     * Sets the dataType attribute to a new value, should never be invoked outside of constructors
     *
     * @param dataType the new {@link DataType}
     */
    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    @Override
    public TextField clone() {
        KeyExpression copy = new KeyExpression(getKeyExpression().getExpressionStart(), getKeyExpression().getExpressionEnd());
        return new TextField(getName(), allowsMultiple(), copy, getDataType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDataType());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TextField textField = (TextField) o;
        return getDataType() == textField.getDataType();
    }

    @Override
    public String toString() {
        return "TextField{" +
                "name='" + getName() + "'" +
                ", allowMultiple=" + allowsMultiple() +
                ", keyExpression=" + getKeyExpression().toString() +
                ", value='" + getValue() + "'" +
                ", dataType=" + dataType +
                "}";
    }
}
