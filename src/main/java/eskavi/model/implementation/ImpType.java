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
    ASSET_CONNECTION(AssetConnection.class, true, -1),
    DESERIALIZER(Deserializer.class, false, -1),
    DISPATCHER(Dispatcher.class, false, -1),
    ENDPOINT(Endpoint.class, true, -1),
    HANDLER(Handler.class, false, -1),
    INTERACTION_STARTER(InteractionStarter.class, true, -1),
    PERSISTENCE_MANAGER(PersistenceManager.class, true,  1),
    SERIALIZER(Serializer.class, false, 1),
    PROTOCOL_TYPE(ProtocolType.class, false, 0),
    MESSAGE_TYPE(MessageType.class, false, 0),
    ENVIRONMENT(Environment.class, true, 1);


    private Class matchingClass;
    @JsonProperty
    private boolean topLevel;
    @JsonProperty
    private int maxUse;

    private ImpType(Class moduleImp, boolean topLevel, int maxUse) {
        this.matchingClass = moduleImp;
        this.topLevel = topLevel;
        this.maxUse = maxUse;
    }

    public String getName() {
        return this.name();
    }

    public boolean matches(ImmutableImplementation input) {
        return matchingClass.isInstance(input);
    }
}
