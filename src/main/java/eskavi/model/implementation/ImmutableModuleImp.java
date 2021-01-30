package eskavi.model.implementation;

import eskavi.model.configuration.Configuration;
import eskavi.model.implementation.moduleimp.*;

import java.util.Collection;

public interface ImmutableModuleImp {
    // TODO: what is instanceConfiguration here for?
    public boolean isCompatible(Collection<ImmutableModuleImp> others, Configuration instanceConfiguration);

    public boolean checkCompatibleEndpoint(Endpoint endpoint);

    public boolean checkCompatibleSerializer(Serializer serializer);

    public boolean checkCompatibleDeserializer(Deserializer deserializer);

    public boolean checkCompatibleDispatcher(Dispatcher dispatcher);

    public boolean checkCompatibleAssetConnection(AssetConnection assetConnection);

    public boolean checkCompatibleHandler(Handler handler);

    public boolean checkCompatiblePersistenceManager(PersistenceManager persistenceManager);

    public boolean checkCompatibleInteractionStarter(InteractionStarter interactionStarter);
}
