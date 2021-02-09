package eskavi.model.implementation;

import eskavi.model.configuration.Configuration;
import eskavi.model.implementation.moduleimp.*;
import eskavi.model.user.User;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.HashSet;

@Entity
public abstract class ModuleImp extends Implementation implements ImmutableModuleImp {
    // TODO consider making configuration an entity to resolve spring error
    private Configuration configuration;

    public ModuleImp() {
    }
    
    public ModuleImp(long implementationId, User author, String name, ImplementationScope impScope, Configuration templateConfiguration) {
        super(implementationId, author, name, impScope);
        this.configuration = templateConfiguration;
    }

    /**
     * Default getter for configuration attribute
     *
     * @return configuration
     */
    public Configuration getConfiguration() {
        return this.configuration;
    }

    /**
     * Default setter for configuration attribute
     *
     * @param configuration configuration to set
     */
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean isCompatible(Collection<ImmutableModuleImp> usedImpCollection) {
        return true;
    }

    @Override
    public boolean checkCompatibleEndpoint(Endpoint endpoint) {
        return true;
    }

    @Override
    public boolean checkCompatibleSerializer(Serializer serializer) {
        return true;
    }

    @Override
    public boolean checkCompatibleDeserializer(Deserializer deserializer) {
        return true;
    }

    @Override
    public boolean checkCompatibleDispatcher(Dispatcher dispatcher) {
        return true;
    }

    @Override
    public boolean checkCompatibleAssetConnection(AssetConnection assetConnection) {
        return true;
    }

    @Override
    public boolean checkCompatibleHandler(Handler handler) {
        return true;
    }

    @Override
    public boolean checkCompatiblePersistenceManager(PersistenceManager persistenceManager) {
        return true;
    }

    @Override
    public boolean checkCompatibleInteractionStarter(InteractionStarter interactionStarter) {
        return true;
    }

    @Override
    public boolean isValid() {
        return super.isValid() && configuration.isValid();
    }

    public HashSet<ImmutableGenericImp> getGenerics() {
        return new HashSet<>();
    }
}
