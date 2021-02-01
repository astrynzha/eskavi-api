package eskavi.model.implementation.moduleimp;

import eskavi.model.configuration.Configuration;
import eskavi.model.implementation.*;
import eskavi.model.user.User;

import java.util.Collection;

public class Serializer extends ModuleImp {
    private MessageType messageType;
    private ProtocolType protocolType;

    public Serializer(long implementationId, User author, String name, Scope scope,
                      MessageType messageType, ProtocolType protocolType) {
        super(implementationId, author, name, scope);
        this.messageType = messageType;
        this.protocolType = protocolType;
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
    public boolean checkCompatibleEndpoint(Endpoint endpoint) {
        return endpoint.getProtocolType().checkCompatibility(protocolType);
    }

    @Override
    public boolean checkCompatibleSerializer(Serializer serializer) {
        return this.equals(serializer);
    }

    @Override
    public boolean checkCompatibleDeserializer(Deserializer deserializer) {
        return deserializer.getMessageType().checkCompatibility(messageType) &&
                deserializer.getProtocolType().checkCompatibility(protocolType);
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
    public boolean isCompatible(Collection<ImmutableModuleImp> usedImpCollection, Configuration instanceConfiguration) {
        for (ImmutableModuleImp usedImp : usedImpCollection) {
            if (!usedImp.checkCompatibleSerializer(this)) {
                return false;
            }
        }
        for (ImmutableModuleImp usedImp : instanceConfiguration.getDependentModuleImps()) {
            if (!usedImp.checkCompatibleSerializer(this)) {
                return false;
            }
        }
        return instanceConfiguration.checkCompatible();
    }
}
