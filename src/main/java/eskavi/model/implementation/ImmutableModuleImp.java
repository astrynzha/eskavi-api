package eskavi.model.implementation;

import eskavi.model.configuration.Configuration;
import eskavi.model.implementation.moduleimp.*;

import java.util.Collection;

public interface ImmutableModuleImp {
    public boolean isCompatible(Collection<ImmutableModuleImp> others, Configuration instanceConfiguration);

    public boolean checkCompatibleEndpoint(Endpoint endpoint);

    public boolean checkCompatibleSerializer(Serializer serializer);

    public boolean checkCompatibleDeserializer(Deserializer deserializer);

    public boolean checkCompatibleDispatcher(Dispatcher dispatcher);

    public boolean checkCompatibleAssetConnection(AssetConnection assetConnection);

    public boolean checkCompatibleHandler(Handler handler);

    public boolean checkCompatiblePersistenceManager(PersistenceManager persistenceManager);

    public boolean checkCompatibleInteractionStarter(InteractionStarter interactionStarter);

    //TODO: Andrii die Methode habe ich (David) hinzugefügt weil ich sie in der Configuration brauche. die muss in den Unterklassen dann implementiert werden.
    //HashSet<ImmutableGenericImp> getGenerics();
}
