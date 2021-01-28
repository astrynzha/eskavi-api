package de.frauenhofer.iosb.eskavi.model.configuration;

import de.frauenhofer.iosb.eskavi.model.implementation.ImmutableModuleImp;

import java.util.Collection;
import java.util.Objects;

/**
 * Class represents the most generic abstraction of a Configuration. All subclasses represent a specific type of
 * Configuration
 */
public abstract class Configuration {
    private String name;
    private boolean allowMultiple;
    private KeyExpression keyExpression;


    /**
     * Constructs a new Configuration.
     *
     * @param name          the name of the Configuration field, which is meant to be displayed to the User
     * @param allowMultiple indicates whether multiple Instances of this Configuration are possible in one Session
     * @param expression    the KeyExpression for this Configuration
     */
    public Configuration(String name, boolean allowMultiple, KeyExpression expression) {
        this.name = name;
        this.allowMultiple = allowMultiple;
        this.keyExpression = expression;
    }

    /**
     * @return String which represents the java Code which has to be included into the Output java Class to use
     * this Configuration.
     */
    public abstract String resolveKeyExpression();

    /**
     * @return boolean whether this Configuration is functional with the given value
     */
    public abstract boolean checkCompatible();

    /**
     * Adds a child Configuration if this Configuration is of type {@link ConfigurationAggregate}, otherwise exception is thrown
     *
     * @param child Configuration to be added to Aggregate
     * @throws IllegalAccessException if method is invoked on object of type other than {@link ConfigurationAggregate}
     */
    public void addChild(Configuration child) throws IllegalAccessException {
        throw new IllegalAccessException("This type of Configuration can't have children");
    }


    /**
     * Removes the child with the given name, if this object is of type {@link ConfigurationAggregate}, otherwise exception is thrown
     * If given configuration is no child, nothing happens
     *
     * @param name of the Configuration to remove
     * @throws IllegalAccessException if method is invoked on object of type other than {@link ConfigurationAggregate}
     */
    public void removeChild(String name) throws IllegalAccessException {
        throw new IllegalAccessException("This type of Configuration doesn't have children");
    }

    /**
     * @return the {@link KeyExpression} of this Configuration
     */
    public KeyExpression getKeyExpression() {
        return keyExpression;
    }

    /**
     * Sets the KeyExpression to the given value, should not be invoked outside of constructors
     *
     * @param expression the new {@link KeyExpression}
     */
    public void setKeyExpression(KeyExpression expression) {
        this.keyExpression = expression;
    }

    public Collection<ImmutableModuleImp> getDependentModuleImps() {
        return null;
    }

    public ImmutableModuleImp getModuleImp() {
        return null;
    }

    /**
     * @return whether this Configuration can be added multiple times. This means multiple times the same fields just
     * different in their value
     */
    public boolean allowsMultiple() {
        return allowMultiple;
    }

    /**
     * Sets the allowMultiple attribute to a new value, should never be invoked outside of constructors
     *
     * @param allowMultiple boolean indicting whether this Configuration can be added multiple times
     */
    public void setAllowMultiple(boolean allowMultiple) {
        this.allowMultiple = allowMultiple;
    }

    /**
     * Name is the String which should be displayed to the user
     *
     * @return the name of this Configuration
     */
    public String getName() {
        return name;
    }


    /**
     * Sets the name attribute to a new value, should never be invoked outside of constructors
     *
     * @param name the new name of the Configuration
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), allowsMultiple(), getKeyExpression());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return allowsMultiple() == that.allowsMultiple() && Objects.equals(getName(), that.getName()) && Objects.equals(getKeyExpression(), that.getKeyExpression());
    }

    @Override
    public abstract Configuration clone();

    @Override
    public String toString() {
        return "Configuration{" +
                "name='" + name + '\'' +
                ", allowMultiple=" + allowMultiple +
                ", keyExpression=" + keyExpression +
                '}';
    }
}
