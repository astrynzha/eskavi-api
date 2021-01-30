package eskavi.model.configuration;

import eskavi.model.implementation.ImmutableGenericImp;
import eskavi.model.implementation.ImmutableModuleImp;
import eskavi.model.implementation.ImpType;
import eskavi.model.implementation.ModuleInstance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * This class represents an Implementation select it enables the user to select an Implementation fitting the stored generics and ImpType.
 */
public class ImplementationSelect extends Configuration {
    private ModuleInstance implementation;
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
     * @param implementation the new instance
     * @throws IllegalArgumentException if the given {@link ModuleInstance} doesn't match the requirements
     */
    public void setImplementation(ModuleInstance implementation) throws IllegalArgumentException {
        if (type.matches(implementation.getModuleImp()) && implementation.getModuleImp().getGenerics().equals(generics)) {
            this.implementation = implementation;
        } else {
            throw new IllegalArgumentException("given ModuleImp doesn't match required type or required generics");
        }
    }

    @Override
    public String resolveKeyExpression() {
        return this.implementation.getInstanceConfiguration().resolveKeyExpression();
    }

    @Override
    public boolean checkCompatible() {
        return this.implementation.getInstanceConfiguration().checkCompatible();
    }

    @Override
    public List<Configuration> getChildren() {
        List<Configuration> result = new ArrayList<>();
        result.add(this.implementation.getInstanceConfiguration());
        return result;
    }

    @Override
    public ImmutableModuleImp getModuleImp() {
        return implementation.getModuleImp();
    }

    @Override
    public Configuration clone() {
        KeyExpression copy = new KeyExpression(getKeyExpression().getExpressionStart(), getKeyExpression().getExpressionEnd());
        return new ImplementationSelect(this.getName(), this.allowsMultiple(), copy, this.generics, this.type);
    }
}
