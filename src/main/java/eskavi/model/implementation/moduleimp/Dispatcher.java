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

@Entity
public class Dispatcher extends ModuleImp {
    @OneToOne
    private MessageType messageType;

    public Dispatcher(MessageType messageType) {
        this.messageType = messageType;
    }

    public Dispatcher(long implementationId, User author, String name, ImplementationScope impScope, Configuration templateConfiguration,
                      MessageType messageType) {
        super(implementationId, author, name, impScope, templateConfiguration);
        this.messageType = messageType;
    }

    public Dispatcher() {

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
            if (!usedImp.checkCompatibleDispatcher(this)) {
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
        return dispatcher.equals(this);
    }

    @Override
    public boolean checkCompatibleHandler(Handler handler) {
        return handler.getMessageType().checkCompatibility(messageType);
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
        Dispatcher that = (Dispatcher) o;
        return Objects.equals(messageType, that.messageType);
    }

    @Override
    public String toString() {
        return "Dispatcher" + super.toString() +
                ", messageType=" + messageType +
                "}";
    }
}
