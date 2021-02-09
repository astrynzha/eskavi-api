package eskavi.model.implementation;

import eskavi.model.implementation.moduleimp.*;

/**
 * This enumeration lists all available subclasses of Implementation and enables
 * communication regarding subclasses with the frontend.
 *
 * @author Andrii Strynzha, David Kaufmann, Maximilian Georg
 * @version 1.0.0
 */
public enum ImpType {
    ASSET_CONNECTION(AssetConnection.class),
    DESERIALIZER(Deserializer.class),
    DISPATCHER(Dispatcher.class),
    ENDPOINT(Endpoint.class),
    HANDLER(Handler.class),
    INTERACTION_STARTER(InteractionStarter.class),
    PERSISTENCE_MANAGER(PersistenceManager.class),
    SERIALIZER(Serializer.class);

    private Class matchingClass;

    private ImpType(Class moduleImp) {
        this.matchingClass = moduleImp;
    }

    public boolean matches(ImmutableModuleImp input) {
        return matchingClass.isInstance(input);
    }
}
