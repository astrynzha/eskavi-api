package eskavi.model.configuration;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import eskavi.model.implementation.ImmutableGenericImp;
import eskavi.model.implementation.ImmutableModuleImp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.*;

/**
 * This class represents an Aggregate of multiple Configuration. This class is part of the composite pattern which makes it
 * very easy to stack Confiurations as part of others.
 */
@Entity
public class ConfigurationAggregate extends Configuration {
    @OneToMany(cascade = CascadeType.ALL)
    private List<Configuration> children;
    private boolean enforceCompatibility;

    /**
     * Constructs a new ConfigurationAggregate
     *
     * @param name                 the name displayed to the user
     * @param allowMultiple        whether this aggregate can be added multiple times
     * @param expression           the {@link KeyExpression} of this Aggregate
     * @param children             can be empty, list of the configurations inside this Aggregate
     * @param enforceCompatibility whether the compatibility between the {@link ImmutableModuleImp}s in this
     *                             Aggregate has to be ensured.
     */
    public ConfigurationAggregate(String name, boolean allowMultiple, KeyExpression expression, List<Configuration> children, boolean enforceCompatibility) {
        super(name, allowMultiple, expression);
        this.children = children;
        this.enforceCompatibility = enforceCompatibility;
    }

    protected ConfigurationAggregate() {
    }

    /**
     * Returns whether the compatibility between the {@link ImmutableModuleImp}s in this Aggregate has to be ensured.
     *
     * @return boolean
     */
    @JsonGetter("enforceCompatibility")
    public boolean enforcesCompatibility() {
        return this.enforceCompatibility;
    }

    protected void setEnforceCompatibility(boolean enforceCompatibility) {
        this.enforceCompatibility = enforceCompatibility;
    }

    private boolean enforceCompatibility() {
        HashSet<ImmutableModuleImp> moduleImps = new HashSet<>();
        for (Configuration config : children) {
            /*config.getModuleImp only returns not null if config is impSelect -> then returns imp
             *attached to moduleInstance. So only implementations added in this Aggregate end up in this Map.
             */
            moduleImps.add(config.getModuleImp());
        }
        moduleImps.remove(null);

        for (ImmutableModuleImp moduleImp : moduleImps) {
            if (!moduleImp.isCompatible(moduleImps)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String resolveKeyExpression() {
        String result = this.getKeyExpression().getExpressionStart();

        for (Configuration config : children) {
            result += config.resolveKeyExpression();
        }
        return result + this.getKeyExpression().getExpressionEnd();
    }

    @Override
    public boolean checkCompatible() {
        for (Configuration config : children) {
            if (!config.checkCompatible()) {
                return false;
            }
        }

        if (enforceCompatibility) {
            return this.enforceCompatibility();
        } else {
            return true;
        }
    }

    @Override
    public boolean isValid() {
        for (Configuration config : children) {
            if (!config.isValid()) return false;
        }

        if (enforceCompatibility) {
            HashSet<ImmutableGenericImp> allGenerics = new HashSet<>();
            for (Configuration config : children) {
                if (config.getClass() == ImplementationSelect.class) {
                    ImplementationSelect impSelect = (ImplementationSelect) config;
                    allGenerics.addAll(impSelect.getGenerics());
                }
            }
            for (ImmutableGenericImp generic : allGenerics) {
                for (ImmutableGenericImp other : allGenerics) {
                    if (!generic.checkCompatibility(other)) return false;
                }
            }
        }
        return true;
    }

    @Override
    public void addChild(Configuration config) {
        if (!children.contains(config) || config.allowsMultiple()) {
            children.add(config);
        } else {
            throw new IllegalArgumentException("the config was already added and can't be added multiple times.");
        }
    }

    @Override
    public void removeChild(String name) {
        children.removeIf(config -> config.getName().equals(name));
    }

    @Override
    public List<Configuration> getChildren() {
        return children;
    }

    @JsonSetter("children")
    public void setChildren(List<Configuration> children) {
        this.children = new LinkedList<>();
        for (Configuration config : children) {
            addChild(config);
        }
    }

    @Override
    public Collection<ImmutableModuleImp> getDependentModuleImps() {
        HashSet<ImmutableModuleImp> result = new HashSet<>();
        for (Configuration child : children) {
            result.addAll(child.getDependentModuleImps());
        }
        result.remove(null);
        return result;
    }

    @Override
    public Collection<ImmutableModuleImp> getRequiredInstances() {
        HashSet<ImmutableModuleImp> result = new HashSet<>();
        for (Configuration child : children) {
            result.addAll(child.getDependentModuleImps());
        }
        result.remove(null);
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), children, enforceCompatibility);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ConfigurationAggregate that = (ConfigurationAggregate) o;
        if (enforceCompatibility != that.enforceCompatibility) {
            return false;
        }
        // check that children of this and that have same fields but it is not necessary that their amount is equal
        // e.g. (textField1, textField1, fileField1, fileField2) equals (textField1, fileField1, fileField2)
        List<Configuration> thatChildren = that.getChildren();
        for (Configuration thatChild : thatChildren) {
            if (!this.children.contains(thatChild)) {
                return false;
            }
        }
        for (Configuration thisChild : this.children) {
            if (!thatChildren.contains(thisChild)) {
                return false;
            }
        }
        return enforceCompatibility == that.enforceCompatibility && Objects.equals(thatChildren, that.children);
    }

    @Override
    public Configuration clone() {
        List<Configuration> clonedChildren = new ArrayList<>();
        for (Configuration config : children) {
            clonedChildren.add(config.clone());
        }

        KeyExpression copy = new KeyExpression(this.getKeyExpression().getExpressionStart(), this.getKeyExpression().getExpressionEnd());

        return new ConfigurationAggregate(this.getName(), this.allowsMultiple(), copy, clonedChildren, this.enforcesCompatibility());
    }

    @Override
    public String toString() {
        return "ConfigurationAggregate{" +
                "name='" + getName() + "'" +
                ", allowMultiple=" + allowsMultiple() +
                ", keyExpression=" + getKeyExpression().toString() +
                ", enforceCompatibility=" + enforceCompatibility +
                ", children=" + children.toString() + "}";
    }
}
