package eskavi.model.implementation.moduleimp;

import eskavi.model.configuration.Configuration;
import eskavi.model.implementation.*;
import eskavi.model.user.User;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

/**
 * This class inherits the abstract class ModuleImp and represents an MI of the type Handler.
 *
 * @author Andrii Strynzha, David Kaufmann, Maximilian Georg
 * @version 1.0.0
 */
@Entity
public class Handler extends ModuleImp {
    @OneToOne
    private MessageType messageType;

    public Handler() {
    }

    public Handler(long implementationId, User author, String name, ImplementationScope impScope, Configuration templateConfiguration,
                   MessageType messageType) {
        super(implementationId, author, name, impScope, templateConfiguration);
        this.messageType = messageType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public boolean isCompatible(Collection<ImmutableModuleImp> usedImpCollection) {
        for (ImmutableModuleImp usedImp : usedImpCollection) {
            if (!usedImp.checkCompatibleHandler(this)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkCompatibleSerializer(Serializer serializer) {
        return serializer.getMessageType().checkCompatibility(messageType);
    }

    @Override
    public boolean checkCompatibleDeserializer(Deserializer deserializer) {
        return deserializer.getMessageType().checkCompatibility(messageType);
    }

    @Override
    public boolean checkCompatibleDispatcher(Dispatcher dispatcher) {
        return dispatcher.getMessageType().checkCompatibility(messageType);
    }

    @Override
    public HashSet<ImmutableGenericImp> getGenerics() {
        return new HashSet<>(Collections.singletonList(messageType));
    }

    @Override
    public boolean isValid() {
        return super.isValid() && messageType != null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), messageType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Handler handler = (Handler) o;
        return Objects.equals(messageType, handler.messageType);
    }

    @Override
    public String toString() {
        return "Handler" + super.toString() +
                ", messageType=" + messageType +
                "}";
    }
}
