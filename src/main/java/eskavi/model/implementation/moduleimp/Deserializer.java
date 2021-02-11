package eskavi.model.implementation.moduleimp;

import eskavi.model.configuration.Configuration;
import eskavi.model.implementation.*;
import eskavi.model.user.User;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * This class inherits the abstract class ModuleImp and represents an MI of the type Deserializer.
 *
 * @author Andrii Strynzha, David Kaufmann, Maximilian Georg
 * @version 1.0.0
 */
@Entity
public class Deserializer extends ModuleImp {
    @OneToOne
    private ProtocolType protocolType;
    @OneToOne
    private MessageType messageType;

    public Deserializer() {
    }

    /**
     * Constructs a deserializer object.
     * @param implementationId id
     * @param author author
     * @param name name
     * @param impScope implementation Scope
     * @param templateConfiguration configuration of this moduleImp
     * @param messageType message type generic
     * @param protocolType protocol type generic
     */
    public Deserializer(long implementationId, User author, String name, ImplementationScope impScope,
                        Configuration templateConfiguration,
                        MessageType messageType, ProtocolType protocolType) {
        super(implementationId, author, name, impScope, templateConfiguration);
        this.protocolType = protocolType;
        this.messageType = messageType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public ProtocolType getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(ProtocolType protocolType) {
        this.protocolType = protocolType;
    }

    @Override
    public boolean isCompatible(Collection<ImmutableModuleImp> usedImpCollection) {
        for (ImmutableModuleImp usedImp : usedImpCollection) {
            if (!usedImp.checkCompatibleDeserializer(this)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkCompatibleEndpoint(Endpoint endpoint) {
        return endpoint.getProtocolType().checkCompatibility(protocolType);
    }

    @Override
    public boolean checkCompatibleSerializer(Serializer serializer) {
        return serializer.getMessageType().checkCompatibility(messageType) &&
                serializer.getProtocolType().checkCompatibility(protocolType);
    }

    @Override
    public boolean checkCompatibleDeserializer(Deserializer deserializer) {
        return deserializer.equals(this);
    }

    @Override
    public boolean checkCompatibleDispatcher(Dispatcher dispatcher) {
        return dispatcher.getMessageType().checkCompatibility(messageType);
    }

    @Override
    public boolean checkCompatibleHandler(Handler handler) {
        return handler.getMessageType().checkCompatibility(messageType);
    }

    @Override
    public HashSet<ImmutableGenericImp> getGenerics() {
        return new HashSet<>(Arrays.asList(protocolType, messageType));
    }

    @Override
    public boolean isValid() {
        return super.isValid() && protocolType != null && messageType != null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), protocolType, messageType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Deserializer that = (Deserializer) o;
        return Objects.equals(protocolType, that.protocolType) && Objects.equals(messageType, that.messageType);
    }

    @Override
    public String toString() {
        return "Deserializer" + super.toString() +
                ", protocolType=" + protocolType +
                ", messageType=" + messageType +
                "}";
    }
}
