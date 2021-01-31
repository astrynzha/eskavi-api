package eskavi.model.configuration;

import eskavi.model.implementation.ImmutableGenericImp;
import eskavi.model.implementation.ImmutableModuleImp;
import eskavi.model.implementation.ImpType;
import eskavi.model.implementation.ModuleInstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

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

    /**
     * Returns the required {@link ImmutableGenericImp}s as a HashSet to not allow the same generic multiple times.
     *
     * @return The required generics for this Configuration
     */
    public HashSet<ImmutableGenericImp> getGeneric() {
        return generics;
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
        if (true/*type.matches(instance.getModuleImp()) && instance.getModuleImp().getGenerics().equals(generics)*/) {
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
        return this.instance != null ? this.instance.getInstanceConfiguration().checkCompatible() : false;
    }

    @Override
    public List<Configuration> getChildren() {
        List<Configuration> result = new ArrayList<>();
        if (instance != null) {
            result.add(this.instance.getInstanceConfiguration());
        }

        return result;
    }

    @Override
    public Collection<ImmutableModuleImp> getDependentModuleImps() {
        HashSet<ImmutableModuleImp> result = new HashSet<>();
        result.add(this.instance.getModuleImp());
        result.addAll(this.instance.getInstanceConfiguration().getDependentModuleImps());
        return result;
    }

    @Override
    public ImmutableModuleImp getModuleImp() {
        return (instance != null) ? instance.getModuleImp() : null;
    }

    @Override
    public Configuration clone() {
        KeyExpression copy = new KeyExpression(getKeyExpression().getExpressionStart(), getKeyExpression().getExpressionEnd());
        return new ImplementationSelect(this.getName(), this.allowsMultiple(), copy, this.generics, this.type);
    }
}
