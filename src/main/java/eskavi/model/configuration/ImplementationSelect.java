package eskavi.model.configuration;

import eskavi.model.implementation.ImmutableGenericImp;
import eskavi.model.implementation.ImmutableModuleImp;
import eskavi.model.implementation.ImpType;
import eskavi.model.implementation.ModuleInstance;

import java.util.ArrayList;
import java.util.List;

public class ImplementationSelect extends Configuration {
    private ModuleInstance implementation;
    private ImmutableGenericImp generic;
    private ImpType type;

    public ImplementationSelect(String name, boolean allowMultiple, KeyExpression expression, ImmutableGenericImp generic,
                                ImpType type) {
        super(name, allowMultiple, expression);
        this.generic = generic;
        this.type = type;
    }

    public ImmutableGenericImp getGeneric() {
        return generic;
    }

    public ImpType getType() {
        return type;
    }

    public ImmutableModuleImp getImplementation() {
        return implementation.getModuleImp();
    }

    public void setImplementation(ModuleInstance implementation) {
        this.implementation = implementation;
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
    public Configuration clone() {
        KeyExpression copy = new KeyExpression(getKeyExpression().getExpressionStart(), getKeyExpression().getExpressionEnd());
        return new ImplementationSelect(this.getName(), this.allowsMultiple(), copy, this.generic, this.type);
    }
}
