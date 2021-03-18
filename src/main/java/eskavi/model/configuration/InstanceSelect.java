package eskavi.model.configuration;

import eskavi.model.implementation.ImmutableGenericImp;
import eskavi.model.implementation.ImmutableModuleImp;
import eskavi.model.implementation.ImpType;

import javax.persistence.Entity;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
    public Collection<ImmutableModuleImp> getRequiredInstances() {
        return Arrays.asList(this.getInstance().getModuleImp());
    }
}
