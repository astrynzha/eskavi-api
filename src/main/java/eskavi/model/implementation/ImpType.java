package eskavi.model.implementation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import eskavi.model.implementation.moduleimp.*;

/**
 * This enumeration lists all available subclasses of Implementation and enables
 * communication regarding subclasses with the frontend.
 *
 * @author Andrii Strynzha, David Kaufmann, Maximilian Georg
 * @version 1.0.0
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "name")
public enum ImpType {
    //TODO Generics
    ASSET_CONNECTION(AssetConnection.class, true),
    DESERIALIZER(Deserializer.class, false),
    DISPATCHER(Dispatcher.class, false),
    ENDPOINT(Endpoint.class, true),
    HANDLER(Handler.class, false),
    INTERACTION_STARTER(InteractionStarter.class, true),
    PERSISTENCE_MANAGER(PersistenceManager.class, true),
    SERIALIZER(Serializer.class, false);

    private Class matchingClass;
    @JsonProperty
    private boolean topLevel;

    private ImpType(Class moduleImp, boolean topLevel) {
        this.matchingClass = moduleImp;
        this.topLevel = topLevel;
    }

    public String getName() {
        return this.name();
    }

    public boolean matches(ImmutableModuleImp input) {
        return matchingClass.isInstance(input);
    }
}
