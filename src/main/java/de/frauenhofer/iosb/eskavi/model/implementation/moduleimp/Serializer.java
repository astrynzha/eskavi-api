package de.frauenhofer.iosb.eskavi.model.implementation.moduleimp;

import de.frauenhofer.iosb.eskavi.model.configuration.Configuration;
import de.frauenhofer.iosb.eskavi.model.implementation.ImmutableModuleImp;
import de.frauenhofer.iosb.eskavi.model.implementation.MessageType;
import de.frauenhofer.iosb.eskavi.model.implementation.ModuleImp;
import de.frauenhofer.iosb.eskavi.model.implementation.ProtocolType;
import de.frauenhofer.iosb.eskavi.model.implementation.Scope;
import de.frauenhofer.iosb.eskavi.model.user.User;

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

  @Override
  public boolean isCompatible(Collection<ImmutableModuleImp> usedImpCollection, Configuration instanceConfiguration) {
    for (ImmutableModuleImp usedImp : usedImpCollection) {
      if (!usedImp.checkCompatibleSerializer(this)) {
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
    return deserializer.getMessageType().checkCompatibility(messageType) &&
            deserializer.getProtocolType().checkCompatibility(protocolType);
  }

  @Override
  public boolean checkCompatibleDispatcher(Dispatcher dispatcher) {
    return dispatcher.getMessageType().checkCompatibility(messageType);
  }

  @Override
  public boolean checkCompatibleAssetConnection(AssetConnection assetConnection) {
    return true;
  }

  @Override
  public boolean checkCompatibleHandler(Handler handler) {
    return handler.getMessageType().checkCompatibility(messageType);
  }

  @Override
  public boolean checkCompatiblePersistenceManager(PersistenceManager persistenceManager) {
    return true;
  }

  @Override
  public boolean checkCompatibleInteractionStarter(InteractionStarter interactionStarter) {
    return true;
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
}
