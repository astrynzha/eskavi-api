package eskavi.model.implementation.moduleimp;

import eskavi.model.implementation.*;
import eskavi.model.user.User;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

@Entity
public class Dispatcher extends ModuleImp {
    @OneToOne
    private MessageType messageType;

    public Dispatcher(MessageType messageType) {
        this.messageType = messageType;
    }

    public Dispatcher(long implementationId, User author,
                      String name, ImplementationScope impScope, MessageType messageType) {
        super(implementationId, author, name, impScope);
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
}
