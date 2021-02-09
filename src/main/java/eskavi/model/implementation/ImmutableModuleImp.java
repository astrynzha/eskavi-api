package eskavi.model.implementation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eskavi.model.configuration.Configuration;
import eskavi.model.implementation.moduleimp.*;

import java.util.Collection;
import java.util.HashSet;

public interface ImmutableModuleImp extends ImmutableImplementation {
    public boolean isCompatible(Collection<ImmutableModuleImp> others);

    public boolean checkCompatibleEndpoint(Endpoint endpoint);

    public boolean checkCompatibleSerializer(Serializer serializer);

    public boolean checkCompatibleDeserializer(Deserializer deserializer);

    public boolean checkCompatibleDispatcher(Dispatcher dispatcher);

    public boolean checkCompatibleAssetConnection(AssetConnection assetConnection);

    public boolean checkCompatibleHandler(Handler handler);

    public boolean checkCompatiblePersistenceManager(PersistenceManager persistenceManager);

    public boolean checkCompatibleInteractionStarter(InteractionStarter interactionStarter);

    /**
     * Default getter for configuration attribute
     *
     * @return configuration
     */
    public Configuration getConfiguration();

    @JsonIgnore
    public HashSet<ImmutableGenericImp> getGenerics();
}
