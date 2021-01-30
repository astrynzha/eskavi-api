package de.frauenhofer.iosb.eskavi.model.configuration;

import de.frauenhofer.iosb.eskavi.model.implementation.ImmutableModuleImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigurationAggregate extends Configuration {
    List<Configuration> children;
    private boolean enforceCompatibility;

    public ConfigurationAggregate(String name, boolean allowMultiple, KeyExpression expression,
                                  List<Configuration> children, boolean enforceCompatibility) {
        super(name, allowMultiple, expression);
        this.children = children;
        this.enforceCompatibility = enforceCompatibility;
    }

    public boolean enforcesCompatibility() {
        return this.enforceCompatibility;
    }

    private boolean enforceCompatibility() {
        HashMap<ImmutableModuleImp, Configuration> moduleImps = new HashMap<>();
        for (Configuration config : children) {
            /*config.getModuleImp only returns not null if config is impSelect -> then first child is Configuration
             *attached to moduleInstance. So only implementations added in this Aggregate end up in this Map.
             */
            moduleImps.put(config.getModuleImp(), config.getChildren().get(0));
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
    public void addChild(Configuration config) {
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
    public Configuration clone() {
        List<Configuration> clonedChildren = new ArrayList<>();
        for (Configuration config : children) {
            clonedChildren.add(config.clone());
        }

        KeyExpression copy = new KeyExpression(this.getKeyExpression().getExpressionStart(), this.getKeyExpression().getExpressionEnd());

        return new ConfigurationAggregate(this.getName(), this.allowsMultiple(), copy, clonedChildren, this.enforcesCompatibility());
    }
}
