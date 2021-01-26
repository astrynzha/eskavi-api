package de.frauenhofer.iosb.eskavi.module.configuration;

import java.util.Collection;

public abstract class Configuration {
    private String name;
    private boolean allowMultiple;
    private KeyExpression keyExpression;

    public Configuration(String name, boolean allowMultiple, KeyExpression expression) {
        this.name = name;
        this.allowMultiple = allowMultiple;
        this.keyExpression = expression;
    }

    public abstract String resolveKeyExpression();
    public abstract boolean checkCompatible();
    @Override
    public abstract Configuration clone();

    public void addChild(Configuration child) {
        //does nothing should not be invoked onto this type
    }

    public void removeChild(String name) {
        //does nothing should not be invoked onto this type
    }
    /*
    public Collection<ImmutableModuleImp> getDependentModuleImps() {
        return null;
    }*/

    public ImmutableModuleImp getModuleImp() {
        return null;
    }

    public KeyExpression getKeyExpression() {
        return keyExpression;
    }

    public void setKeyExpression(KeyExpression expression) {
        this.keyExpression = expression;
    }

    public boolean allowsMultiple() {
        return allowMultiple;
    }

    public void setAllowMultiple(boolean allowMultiple) {
        this.allowMultiple = allowMultiple;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
