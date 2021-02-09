package eskavi.model.implementation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eskavi.model.implementation.moduleimp.*;

import java.util.Collection;
import java.util.HashSet;

/**
 * This interface inherits ImmutableImplementation and makes Module Implementations available.
 * However, the Module Implementations cannot be changed via this interface.
 *
 * @author Andrii Strynzha, David Kaufmann, Maximilian Georg
 * @version 1.0.0
 */
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

    @JsonIgnore
    public HashSet<ImmutableGenericImp> getGenerics();
}
