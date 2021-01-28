package de.frauenhofer.iosb.eskavi.model.configuration;

import java.util.Objects;

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
    }

    public ImmutableModuleImp getModuleImp() {
        return null;
    }*/

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

    @Override
    public String toString() {
        return "Configuration{" +
                "name='" + name + '\'' +
                ", allowMultiple=" + allowMultiple +
                ", keyExpression=" + keyExpression +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return allowsMultiple() == that.allowsMultiple() && Objects.equals(getName(), that.getName()) && Objects.equals(getKeyExpression(), that.getKeyExpression());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), allowsMultiple(), getKeyExpression());
    }
}
