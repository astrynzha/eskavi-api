package de.frauenhofer.iosb.eskavi.model.implementation;

import de.frauenhofer.iosb.eskavi.model.configuration.Configuration;
import de.frauenhofer.iosb.eskavi.model.user.User;

import java.util.Collection;

// TODO: is the Configuration attribute needed?
public abstract class ModuleImp extends Implementation implements ImmutableModuleImp {
  private MessageType messageType;
  private ProtocolType protocolType;

  protected ModuleImp(long implementationId, User author, String name, Scope scope, MessageType messageType, ProtocolType protocolType) {
    super(implementationId, author, name, scope);
    this.messageType = messageType;
    this.protocolType = protocolType;
  }

  @Override
  public abstract boolean isCompatible(Collection<ImmutableModuleImp> usedImps, Configuration instanceConfiguration);

  @Override
  public MessageType getMessageType() {
    return messageType;
  }

  @Override
  public ProtocolType getProtocolType() {
    return protocolType;
  }
}
