package eskavi.model.implementation.moduleimp;

import eskavi.model.implementation.*;
import eskavi.model.user.User;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class Handler extends ModuleImp {
    private MessageType messageType;

    public Handler() {
    }

    public Handler(long implementationId, User author, String name, ImplementationScope impScope, MessageType messageType) {
        super(implementationId, author, name, impScope);
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
}
