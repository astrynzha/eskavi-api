package eskavi.model.configuration;

import eskavi.model.implementation.ImmutableGenericImp;
import eskavi.model.implementation.ImmutableModuleImp;
import eskavi.model.implementation.ImplementationScope;
import eskavi.model.implementation.Scope;
import eskavi.model.implementation.moduleimp.*;
import eskavi.model.user.ImmutableUser;

import java.util.Collection;
import java.util.HashSet;

public class ConfigurationImplementationStub implements ImmutableModuleImp {
    private Configuration instanceConfig;
    private int compatible;
    private ImmutableGenericImp genericImp;

    public ConfigurationImplementationStub(int compatible, ImmutableGenericImp generic, Configuration instanceConfig) {
        this.compatible = compatible;
        this.genericImp = generic;
        this.instanceConfig = instanceConfig;
    }

    public int getCompatible() {
        return compatible;
    }

    @Override
    public long getImplementationId() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public ImmutableUser getAuthor() {
        return null;
    }

    @Override
    public Scope getScope() {
        return null;
    }

    @Override
    public Collection<ImmutableUser> getUsers() {
        return null;
    }

    @Override
    public boolean isSubscribed(ImmutableUser user) {
        return false;
    }

    @Override
    public ImplementationScope getImplementationScope() {
        return null;
    }

    @Override
    public boolean isCompatible(Collection<ImmutableModuleImp> others) {
        for (ImmutableModuleImp other : others) {
            ConfigurationImplementationStub otherStub = (ConfigurationImplementationStub) other;
            if (this.compatible != otherStub.getCompatible()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkCompatibleEndpoint(Endpoint endpoint) {
        return false;
    }

    @Override
    public boolean checkCompatibleSerializer(Serializer serializer) {
        return false;
    }

    @Override
    public boolean checkCompatibleDeserializer(Deserializer deserializer) {
        return false;
    }

    @Override
    public boolean checkCompatibleDispatcher(Dispatcher dispatcher) {
        return false;
    }

    @Override
    public boolean checkCompatibleAssetConnection(AssetConnection assetConnection) {
        return false;
    }

    @Override
    public boolean checkCompatibleHandler(Handler handler) {
        return false;
    }

    @Override
    public boolean checkCompatiblePersistenceManager(PersistenceManager persistenceManager) {
        return false;
    }

    @Override
    public boolean checkCompatibleInteractionStarter(InteractionStarter interactionStarter) {
        return false;
    }

    @Override
    public Configuration getConfiguration() {
        return instanceConfig;
    }

    @Override
    public HashSet<ImmutableGenericImp> getGenerics() {
        HashSet<ImmutableGenericImp> result = new HashSet<>();
        result.add(genericImp);
        return result;
    }
}
