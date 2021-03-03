package eskavi.model.implementation;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import eskavi.model.implementation.moduleimp.*;
import eskavi.model.user.ImmutableUser;
import eskavi.model.user.User;

import java.util.Collection;

/**
 * This interface is used to work with implementations.
 * However, it is not possible to change the implementation via the interface.
 *
 * @author Andrii Strynzha, David Kaufmann, Maximilian Georg
 * @version 1.0.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "jsonTypeInfo")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AssetConnection.class, name = "ASSET_CONNECTION"),
        @JsonSubTypes.Type(value = Deserializer.class, name = "DESERIALIZER"),
        @JsonSubTypes.Type(value = Dispatcher.class, name = "DISPATCHER"),
        @JsonSubTypes.Type(value = Endpoint.class, name = "ENDPOINT"),
        @JsonSubTypes.Type(value = Handler.class, name = "HANDLER"),
        @JsonSubTypes.Type(value = InteractionStarter.class, name = "INTERACTION_STARTER"),
        @JsonSubTypes.Type(value = PersistenceManager.class, name = "PERSISTENCE_MANAGER"),
        @JsonSubTypes.Type(value = Serializer.class, name = "SERIALIZER"),
        @JsonSubTypes.Type(value = ProtocolType.class, name = "PROTOCOL_TYPE"),
        @JsonSubTypes.Type(value = MessageType.class, name = "MESSAGE_TYPE"),
        @JsonSubTypes.Type(value = Environment.class, name = "ENVIRONMENT")
})
public interface ImmutableImplementation {
    public long getImplementationId();

    public String getName();

    public ImmutableUser getAuthor();
    /**
     * @return list of all the users that are subscribed to this Implementation
     */
    public Collection<User> getSubscribed();

    public boolean isSubscribed(ImmutableUser user);

    public ImplementationScope getImplementationScope();
}
