package eskavi.model.configuration;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonSetter;
import eskavi.model.implementation.*;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import java.util.*;

@Entity
public class InstanceSelect extends Configuration {
    @Transient
    private ImmutableModuleImp moduleImp;
    @ManyToMany(targetEntity = GenericImp.class)
    private Set<ImmutableGenericImp> generics;
    @JsonIdentityReference(alwaysAsId = true)
    private ImpType type;

    public InstanceSelect(String name, boolean allowMultiple, KeyExpression expression, Set<ImmutableGenericImp> generic,
                          ImpType type) {
        super(name, allowMultiple, expression);
        this.generics = generic;
        this.type = type;
    }

    protected InstanceSelect() {}

    public ImmutableModuleImp getModuleImp() {
        return moduleImp;
    }

    public void setModuleImp(ImmutableModuleImp moduleImp) {
        this.moduleImp = moduleImp;
    }

    public Set<ImmutableGenericImp> getGenerics() {
        return generics;
    }

    @JsonSetter
    public void setGenerics(Set<ImmutableGenericImp> generics) {
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

    @Override
    public String resolveKeyExpression() {
        if (getModuleImp() != null) {
            return this.getKeyExpression().getExpressionStart() + this.getModuleImp().getName().toLowerCase()
                    + this.getKeyExpression().getExpressionEnd();
        } else {
            throw new IllegalStateException("Instance has to be set");
        }
    }

    @Override
    public boolean checkCompatible() {
        return this.getModuleImp() != null;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isValidJavaCode() {
        return this.getKeyExpression().isValid();
    }

    @Override
    public Collection<ImmutableModuleImp> getRequiredInstances() {
        return Arrays.asList(this.getModuleImp());
    }

    @Override
    public InstanceSelect clone() {
        InstanceSelect result = new InstanceSelect(this.getName(), this.allowsMultiple(), this.getKeyExpression(), this.getGenerics(), this.getType());
        if (this.getModuleImp() != null) {
            result.setModuleImp(this.getModuleImp());
        }
        return result;
    }
}
