package eskavi.model.configuration;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eskavi.deserializer.GenericsDeserializer;
import eskavi.model.implementation.ImmutableGenericImp;
import eskavi.model.implementation.ImmutableModuleImp;
import eskavi.model.implementation.ImpType;
import eskavi.model.implementation.ModuleInstance;

import java.util.*;

/**
 * This class represents an Implementation select it enables the user to select an Implementation fitting the stored generics and ImpType.
 */
public class ImplementationSelect extends Configuration {
    private ModuleInstance instance;
    private HashSet<ImmutableGenericImp> generics;
    private ImpType type;

    /**
     * Constructs a new ImplementationSelect.
     *
     * @param name          the name of this Configuration
     * @param allowMultiple whether this Configuration can be used multiple times
     * @param expression    the {@link KeyExpression} of this Configuration
     * @param generic       The required generics for this Configuration.
     * @param type          the required {@link ImpType} for this Configuration
     */
    public ImplementationSelect(String name, boolean allowMultiple, KeyExpression expression, HashSet<ImmutableGenericImp> generic,
                                ImpType type) {
        super(name, allowMultiple, expression);
        this.generics = generic;
        this.type = type;
    }

    protected ImplementationSelect() {

    }

    /**
     * Returns the required {@link ImmutableGenericImp}s as a HashSet to not allow the same generic multiple times.
     *
     * @return The required generics for this Configuration
     */
    @JsonIdentityReference(alwaysAsId = true)
    public HashSet<ImmutableGenericImp> getGenerics() {
        return generics;
    }

    @JsonSetter
    @JsonDeserialize(using = GenericsDeserializer.class)
    public void setGenerics(HashSet<ImmutableGenericImp> generics) {
        this.generics = generics;
    }

    /**
     * Returns the required {@link ImpType} for this Configuration
     *
     * @return the type required for this Select
     */
    public ImpType getType() {
        return type;
    }

    /**
     * Sets the selectedImp to the given {@link ModuleInstance}, if it matches the required type and generics.
     *
     * @param instance the new instance
     * @throws IllegalArgumentException if the given {@link ModuleInstance} doesn't match the requirements
     */
    //TODO test and add serious if
    public void setInstance(ModuleInstance instance) throws IllegalArgumentException {
        if (/*type.matches(instance.getModuleImp()) && */instance.getModuleImp().getGenerics().equals(generics)) {
            this.instance = instance;
        } else {
            throw new IllegalArgumentException("given ModuleImp doesn't match required type or required generics");
        }
    }

    //TODO discuss whether this Exception is meant to be thrown
    @Override
    public String resolveKeyExpression() {
        if (instance != null) {
            return this.getKeyExpression().getExpressionStart() +
                    this.instance.getInstanceConfiguration().resolveKeyExpression() +
                    this.getKeyExpression().getExpressionEnd();
        } else {
            throw new IllegalStateException("instance has to be set");
        }
    }

    @Override
    public boolean checkCompatible() {
        return this.instance != null && this.instance.getInstanceConfiguration().checkCompatible()
                && this.instance.getModuleImp().isCompatible(this.instance.getInstanceConfiguration().getDependentModuleImps());
    }

    @Override
    public List<Configuration> getChildren() {
        List<Configuration> result = new ArrayList<>();
        if (instance != null) {
            result.add(this.instance.getInstanceConfiguration());
        }

        return result;
    }

    @JsonIgnore
    @Override
    public Collection<ImmutableModuleImp> getDependentModuleImps() {
        HashSet<ImmutableModuleImp> result = new HashSet<>();
        result.add(this.instance.getModuleImp());
        result.addAll(this.instance.getInstanceConfiguration().getDependentModuleImps());
        return result;
    }

    @Override
    @JsonIdentityReference(alwaysAsId = true)
    public ImmutableModuleImp getModuleImp() {
        return (instance != null) ? instance.getModuleImp() : null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), instance, generics, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ImplementationSelect that = (ImplementationSelect) o;
        return Objects.equals(instance, that.instance) && Objects.equals(generics, that.generics) && type == that.type;
    }

    @Override
    public Configuration clone() {
        KeyExpression copy = new KeyExpression(getKeyExpression().getExpressionStart(), getKeyExpression().getExpressionEnd());
        return new ImplementationSelect(this.getName(), this.allowsMultiple(), copy, this.generics, this.type);
    }

    public String toString() {
        return "ImplementationSelect{" +
                "name='" + getName() + "'" +
                ", allowMultiple=" + allowsMultiple() +
                ", keyExpression=" + getKeyExpression().toString() +
                ", generics=" + getGenerics().toString() +
                ", type=" + type.name() +
                ", instance=" + instance.toString() + "}";
    }
}
