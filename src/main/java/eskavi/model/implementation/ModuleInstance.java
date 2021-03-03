package eskavi.model.implementation;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import eskavi.model.configuration.Configuration;

import java.util.Collection;
import java.util.Objects;

/**
 * This class represents the ModuleImp when added to an AASSession. The configuration can only be edited on this type of
 * ModuleImp. It still holds all the function a ModuleImp has.
 */
public class ModuleInstance {
    //@JsonDeserialize(using = ImplementationByIdDeserializer.class)
    //@JsonIdentityReference(alwaysAsId = true)
    private ImmutableModuleImp moduleImp;
    private Configuration instanceConfiguration;

    /**
     * Constructs a new ModuleInstance
     *
     * @param moduleImp the ModuleImp it is an instance of
     */
    public ModuleInstance(ImmutableModuleImp moduleImp) {
        this.moduleImp = moduleImp;
        this.instanceConfiguration = moduleImp.getConfigurationRoot().clone();
    }

    protected ModuleInstance() {

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
        return this.moduleImp.isCompatible(others) && this.moduleImp.isCompatible(instanceConfiguration.getDependentModuleImps())
                && instanceConfiguration.checkCompatible();
    }

    /**
     * Returns the {@link ImmutableModuleImp} this is an Instance of
     *
     * @return the {@link ImmutableModuleImp}
     */
    public ImmutableModuleImp getModuleImp() {
        return this.moduleImp;
    }

    @JsonSetter
    protected void setModuleImp(ImmutableModuleImp imp) {
        this.moduleImp = imp;
    }

    /**
     * Returns this Instances Configuration
     *
     * @return the Configuration of this Instance
     */
    @JsonGetter
    public Configuration getInstanceConfiguration() {
        return this.instanceConfiguration;
    }

    /**
     * Sets this Instances Configuration to the given one
     *
     * @param instanceConfiguration new Configuration
     * @throws IllegalArgumentException if the given configuration doesnt match this imps template configuration
     */
    //TODO reestablish have to rework the equals because allowMultiple causes problems. children returns false
    @JsonSetter
    public void setInstanceConfiguration(Configuration instanceConfiguration) {
        /*
        if (instanceConfiguration.equals(this.moduleImp.getConfigurationRoot())) {
            this.instanceConfiguration = instanceConfiguration;
        } else {
            throw new IllegalArgumentException("Configuration has to match the template configuration.");
        }
         */
        this.instanceConfiguration = instanceConfiguration;
    }

    @JsonIgnore
    public long getImpId() {
        return moduleImp.getImplementationId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleImp, instanceConfiguration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModuleInstance that = (ModuleInstance) o;
        return Objects.equals(moduleImp, that.moduleImp) && Objects.equals(instanceConfiguration, that.instanceConfiguration);
    }

    @Override
    public String toString() {
        return "ModuleInstance{" +
                ", moduleImp=" + moduleImp.toString() +
                ", instanceConfiguration=" + instanceConfiguration.toString();
    }
}
