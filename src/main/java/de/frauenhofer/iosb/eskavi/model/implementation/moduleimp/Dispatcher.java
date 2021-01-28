package de.frauenhofer.iosb.eskavi.model.implementation.moduleimp;

import de.frauenhofer.iosb.eskavi.model.configuration.Configuration;
import de.frauenhofer.iosb.eskavi.model.implementation.ImmutableModuleImp;
import de.frauenhofer.iosb.eskavi.model.implementation.MessageType;
import de.frauenhofer.iosb.eskavi.model.implementation.ModuleImp;
import de.frauenhofer.iosb.eskavi.model.implementation.Scope;
import de.frauenhofer.iosb.eskavi.model.user.User;

import java.util.Collection;

public class Dispatcher extends ModuleImp {
  private MessageType messageType;

  public Dispatcher(long implementationId, User author, String name, Scope scope, MessageType messageType) {
    super(implementationId, author, name, scope);
    this.messageType = messageType;
  }

  @Override
  public boolean checkCompatibleEndpoint(Endpoint endpoint) {
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

  @Override
  public boolean isCompatible(Collection<ImmutableModuleImp> usedImpCollection, Configuration instanceConfiguration) {
    for (ImmutableModuleImp usedImp : usedImpCollection) {
      if (!usedImp.checkCompatibleDispatcher(this)) {
        return false;
      }
    }
    return true;
  }

  public MessageType getMessageType() {
    return messageType;
  }

  public void setMessageType(MessageType messageType) {
    this.messageType = messageType;
  }
}
