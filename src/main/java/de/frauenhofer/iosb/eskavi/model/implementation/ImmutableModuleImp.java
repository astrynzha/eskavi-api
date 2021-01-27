package de.frauenhofer.iosb.eskavi.model.implementation;

import de.frauenhofer.iosb.eskavi.model.configuration.Configuration;

import java.util.Collection;

public interface ImmutableModuleImp {
  public Collection<ImmutableGenericImp> getGenerics();

  public boolean isCompatible(Collection<ImmutableModuleImp> others, Configuration instanceConfiguration);

  public MessageType getMessageType();

  public ProtocolType getProtocolType();
}
