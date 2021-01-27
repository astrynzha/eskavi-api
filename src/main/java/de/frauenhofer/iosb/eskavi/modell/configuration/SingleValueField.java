package de.frauenhofer.iosb.eskavi.modell.configuration;

public abstract class SingleValueField extends Configuration {

    private String value;

    public SingleValueField(String name, boolean allowMultiple, KeyExpression expression) {
        super(name, allowMultiple, expression);
    }

    @Override
    public boolean checkCompatible() {
        return true;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
