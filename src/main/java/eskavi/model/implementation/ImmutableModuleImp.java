package eskavi.model.implementation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eskavi.model.configuration.Configuration;
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
    /**
     * Check if the received other MIs are compatible with this MI
     * @param others MIs to check for compatibility with this MI
     * @return true if compatible
     */
    public boolean isCompatible(Collection<ImmutableModuleImp> others);

    /**
     * check if the received endpoint is compatible with this MI
     */
    public boolean checkCompatibleEndpoint(Endpoint endpoint);

    /**
     * check if the received serializer is compatible with this MI
     */
    public boolean checkCompatibleSerializer(Serializer serializer);

    /**
     * check if the received deserializer is compatible with this MI
     */
    public boolean checkCompatibleDeserializer(Deserializer deserializer);

    /**
     * check if the received dispatcher is compatible with this MI
     */
    public boolean checkCompatibleDispatcher(Dispatcher dispatcher);

    /**
     * check if the received asset connection is compatible with this MI
     */
    public boolean checkCompatibleAssetConnection(AssetConnection assetConnection);

    /**
     * check if the received handler is compatible with this MI
     */
    public boolean checkCompatibleHandler(Handler handler);

    /**
     * check if the received persistence Manager is compatible with this MI
     */
    public boolean checkCompatiblePersistenceManager(PersistenceManager persistenceManager);

    /**
     * check if the received interaction starter is compatible with this MI
     */
    public boolean checkCompatibleInteractionStarter(InteractionStarter interactionStarter);

    /**
     * Default getter for configuration attribute
     *
     * @return configuration
     */
    public Configuration getConfigurationRoot();

    @JsonIgnore
    public HashSet<ImmutableGenericImp> getGenerics();
}
