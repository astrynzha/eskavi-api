package eskavi.model.configuration;

import eskavi.model.implementation.ImmutableGenericImp;
import eskavi.model.implementation.ImmutableModuleImp;
import eskavi.model.implementation.ImpType;

import javax.persistence.Entity;
import java.util.*;

@Entity
public class InstanceSelect extends ImplementationSelect {
    public InstanceSelect(String name, boolean allowMultiple, KeyExpression expression, Set<ImmutableGenericImp> generic,
                                       ImpType type) {
        super(name, allowMultiple, expression, generic, type);
    }

    protected InstanceSelect() {}

    @Override
    public String resolveKeyExpression() {
        if (getInstance() != null) {
            return this.getKeyExpression().getExpressionStart() + this.getInstance().getModuleImp().getName().toLowerCase()
                    + this.getKeyExpression().getExpressionEnd();
        } else {
            throw new IllegalStateException("Instance has to be set");
        }
    }

    @Override
    public boolean checkCompatible() {
        return this.getInstance() != null;
    }

    @Override
    public List<Configuration> getChildren() {
        return null;
    }

    @Override
    public Collection<ImmutableModuleImp> getDependentModuleImps() {
        return null;
    }

    @Override
    public ImmutableModuleImp getModuleImp() {
        return null;
    }

    @Override
    public Collection<ImmutableModuleImp> getRequiredInstances(ImmutableModuleImp imp) {
        List<ImmutableModuleImp> result = new ArrayList<>();
        if (hasCircularRequirements(imp)) {
            throw new IllegalStateException("There are circular requirements present");
        }
        result.add(this.getInstance().getModuleImp());
        result.addAll(this.getInstance().getInstanceConfiguration().getRequiredInstances(imp));
        return result;
    }

    @Override
    public boolean hasCircularRequirements(ImmutableModuleImp imp) {
        return this.getInstance() != null && this.getInstance().getModuleImp().equals(imp);
    }

    @Override
    public InstanceSelect clone() {
        InstanceSelect result = new InstanceSelect(this.getName(), this.allowsMultiple(), this.getKeyExpression(), this.getGenerics(), this.getType());
        if (this.getInstance() != null) {
            result.setInstance(this.getInstance());
        }
        return result;
    }
}
