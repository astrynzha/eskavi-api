package eskavi.model.implementation;

import eskavi.model.implementation.moduleimp.*;

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
