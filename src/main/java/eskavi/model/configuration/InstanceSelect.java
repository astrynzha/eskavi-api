package eskavi.model.configuration;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonSetter;
import eskavi.model.implementation.GenericImp;
import eskavi.model.implementation.ImmutableGenericImp;
import eskavi.model.implementation.ImmutableModuleImp;
import eskavi.model.implementation.ImpType;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

@Entity
public class InstanceSelect extends Configuration {
    @Transient
    private ImmutableModuleImp moduleImp;
    @ManyToMany(targetEntity = GenericImp.class)
    @ColumnDefault("0")
    private Set<ImmutableGenericImp> generics;
    @JsonIdentityReference(alwaysAsId = true)
    private ImpType type;

    public InstanceSelect(String name, boolean allowMultiple, KeyExpression expression, Set<ImmutableGenericImp> generic,
                          ImpType type) {
        super(name, allowMultiple, expression);
        this.generics = generic;
        this.type = type;
    }

    protected InstanceSelect() {
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
    public Collection<ImmutableModuleImp> getRequiredInstances() {
        return Arrays.asList(this.getModuleImp());
    }

    @JsonGetter
    public ImmutableModuleImp getModuleImp() {
        return moduleImp;
    }

    @JsonSetter
    public void setModuleImp(ImmutableModuleImp moduleImp) {
        if (type.matches(moduleImp) && (generics.isEmpty() || moduleImp.getGenerics().equals(generics))) {
            this.moduleImp = moduleImp;
        } else {
            throw new IllegalArgumentException("given ModuleImp doesn't match required type or required generics");
        }
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
