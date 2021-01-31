package eskavi.model.configuration;

import eskavi.model.implementation.ImmutableModuleImp;

import java.util.*;

/**
 * This class represents an Aggregate of multiple Configuration. This class is part of the composite pattern which makes it
 * very easy to stack Confiurations as part of others.
 */
public class ConfigurationAggregate extends Configuration {
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
    public ConfigurationAggregate(String name, boolean allowMultiple, KeyExpression expression,
                                  List<Configuration> children, boolean enforceCompatibility) {
        super(name, allowMultiple, expression);
        this.children = children;
        this.enforceCompatibility = enforceCompatibility;
    }

    /**
     * Returns whether the compatibility between the {@link ImmutableModuleImp}s in this Aggregate has to be ensured.
     *
     * @return boolean
     */
    public boolean enforcesCompatibility() {
        return this.enforceCompatibility;
    }

    private boolean enforceCompatibility() {
        HashMap<ImmutableModuleImp, Configuration> moduleImps = new HashMap<>();
        for (Configuration config : children) {
            /*config.getModuleImp only returns not null if config is impSelect -> then first child is Configuration
             *attached to moduleInstance. So only implementations added in this Aggregate end up in this Map.
             */
            moduleImps.put(config.getModuleImp(), config.getChildren() != null ? config.getChildren().get(0) : null);
        }
        moduleImps.remove(null);

        for (ImmutableModuleImp moduleImp : moduleImps.keySet()) {
            if (!moduleImp.isCompatible(moduleImps.keySet(), moduleImps.get(moduleImp))) {
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
    public void addChild(Configuration config) throws IllegalArgumentException {
        if (!children.contains(config) || config.allowsMultiple()) {
            children.add(config);
        } else {
            throw new IllegalArgumentException("the config was already added and can't be added multiple times.");
        }
    }

    @Override
    public void removeChild(String name) {
        for (Configuration config : children) {
            if (config.getName().equals(name)) {
                children.remove(config);
            }
        }
    }

    @Override
    public List<Configuration> getChildren() {
        return children;
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
    public int hashCode() {
        return Objects.hash(super.hashCode(), children, enforceCompatibility);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ConfigurationAggregate that = (ConfigurationAggregate) o;
        return enforceCompatibility == that.enforceCompatibility && Objects.equals(children, that.children);
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
