package eskavi.model.configuration;

import eskavi.model.implementation.ImmutableGenericImp;
import eskavi.model.implementation.ImmutableModuleImp;
import eskavi.model.implementation.ImpType;
import eskavi.model.implementation.ModuleInstance;

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
    public Collection<ModuleInstance> getRequiredInstances(ImmutableModuleImp imp) {
        List<ModuleInstance> result = new ArrayList<>();
        if (hasCircularRequirements(imp)) {
            throw new IllegalStateException("There are circular requirements present");
        }
        result.add(this.getInstance());
        result.addAll(this.getInstance().getInstanceConfiguration().getRequiredInstances(imp));
        return result;
    }

    @Override
    public boolean hasCircularRequirements(ImmutableModuleImp imp) {
        if (this.getInstance() == null) return false;
        if (this.getInstance().getModuleImp().equals(imp))  return true;
        if (this.getInstance().getInstanceConfiguration().hasCircularRequirements(imp)) return true;
        return false;
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
