package de.frauenhofer.iosb.eskavi.model.configuration;

import java.util.Objects;

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
    public TextField clone() {
        KeyExpression copy = new KeyExpression(getKeyExpression().getExpressionStart(), getKeyExpression().getExpressionEnd());
        return new TextField(getName(), allowsMultiple(), copy, getDataType());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TextField textField = (TextField) o;
        return getDataType() == textField.getDataType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDataType());
    }
}
