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
    ASSET_CONNECTION(AssetConnection.class, true, -1, true),
    DESERIALIZER(Deserializer.class, false, -1, true),
    DISPATCHER(Dispatcher.class, false, -1, true),
    ENDPOINT(Endpoint.class, true, -1, false),
    HANDLER(Handler.class, false, -1, true),
    INTERACTION_STARTER(InteractionStarter.class, true, -1, true),
    PERSISTENCE_MANAGER(PersistenceManager.class, true,  1, false),
    SERIALIZER(Serializer.class, false, 1, true),
    PROTOCOL_TYPE(ProtocolType.class, false, 0, true),
    MESSAGE_TYPE(MessageType.class, false, 0, true),
    ENVIRONMENT(Environment.class, true, 1, false);


    private Class matchingClass;
    @JsonProperty
    private boolean topLevel;
    @JsonProperty
    private int maxUse;
    @JsonProperty
    private boolean optional;


    private ImpType(Class moduleImp, boolean topLevel, int maxUse, boolean optional) {
        this.matchingClass = moduleImp;
        this.topLevel = topLevel;
        this.maxUse = maxUse;
        this.optional = optional;
    }

    public String getName() {
        return this.name();
    }

    public boolean matches(ImmutableImplementation input) {
        return matchingClass.isInstance(input);
    }
}
