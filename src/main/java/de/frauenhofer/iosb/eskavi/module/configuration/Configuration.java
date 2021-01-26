package de.frauenhofer.iosb.eskavi.module.configuration;

public abstract class Configuration {
    private String name;
    private boolean allowMultiple;
    private KeyExpression keyExpression;

    public Configuration(String name, boolean allowMultiple, KeyExpression expression) {
        this.name = name;
        this.allowMultiple = allowMultiple;
        this.keyExpression = expression;
    }

    public void addChild(Configuration child) {
        //does nothing should not be invoked onto this type
    }
}
