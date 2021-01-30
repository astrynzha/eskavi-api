package de.frauenhofer.iosb.eskavi.model.implementation;

import de.frauenhofer.iosb.eskavi.model.configuration.Configuration;

import java.util.Collection;

/**
 * This class represents the ModuleImp when added to an AASSession. The configuration can only be edited on this type of
 * ModuleImp. It still holds all the function a ModuleImp has.
 */
public class ModuleInstance {
    private ImmutableModuleImp moduleImp;
    private Configuration instanceConfiguration;

    /**
     * Constructs a new ModuleInstance
     *
     * @param moduleImp             the ModuleImp it is an instance of
     * @param instanceConfiguration the cloned Configuration which is editable
     */
    public ModuleInstance(ImmutableModuleImp moduleImp, Configuration instanceConfiguration) {
        this.moduleImp = moduleImp;
        this.instanceConfiguration = instanceConfiguration;
    }

    /**
     * Resolves the Configuration.
     *
     * @return a String that can be added to the java Class directly
     */
    public String resolveConfiguration() {
        return this.instanceConfiguration.resolveKeyExpression();
    }

    /**
     * Checks whether this instance is compatible with the set of other Imps given
     *
     * @param others Collection of other instances
     * @return true if this Instance is compatible with the other ones, and the Configuration is compatible
     */
    public boolean isCompatible(Collection<ImmutableModuleImp> others) {
        return this.moduleImp.isCompatible(others, this.instanceConfiguration);
    }

    /**
     * Returns the {@link ImmutableModuleImp} this is an Instance of
     *
     * @return the {@link ImmutableModuleImp}
     */
    public ImmutableModuleImp getModuleImp() {
        return this.moduleImp;
    }

    /**
     * Returns this Instances Configuration
     *
     * @return the Configuration of this Instance
     */
    public Configuration getInstanceConfiguration() {
        return this.instanceConfiguration;
    }

    /**
     * Sets this Instances Configuration to the given one
     *
     * @param instanceConfiguration new Configuration
     */
    public void setInstanceConfiguration(Configuration instanceConfiguration) {
        this.instanceConfiguration = instanceConfiguration;
    }
}
