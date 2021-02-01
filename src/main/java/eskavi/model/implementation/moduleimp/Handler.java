package eskavi.model.implementation.moduleimp;

import eskavi.model.configuration.Configuration;
import eskavi.model.implementation.ImmutableModuleImp;
import eskavi.model.implementation.MessageType;
import eskavi.model.implementation.ModuleImp;
import eskavi.model.implementation.Scope;
import eskavi.model.user.User;

import java.util.Collection;

public class Handler extends ModuleImp {
    private MessageType messageType;

    public Handler(long implementationId, User author, String name, Scope scope, MessageType messageType) {
        super(implementationId, author, name, scope);
        this.messageType = messageType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
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
    public boolean isCompatible(Collection<ImmutableModuleImp> usedImpCollection, Configuration instanceConfiguration) {
        for (ImmutableModuleImp usedImp : usedImpCollection) {
            if (!usedImp.checkCompatibleHandler(this)) {
                return false;
            }
        }
        for (ImmutableModuleImp usedImp : instanceConfiguration.getDependentModuleImps()) {
            if (!usedImp.checkCompatibleHandler(this)) {
                return false;
            }
        }
        return instanceConfiguration.checkCompatible();
    }
}
