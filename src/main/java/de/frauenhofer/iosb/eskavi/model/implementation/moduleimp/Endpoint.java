package de.frauenhofer.iosb.eskavi.model.implementation.moduleimp;

import de.frauenhofer.iosb.eskavi.model.configuration.Configuration;
import de.frauenhofer.iosb.eskavi.model.implementation.ImmutableModuleImp;
import de.frauenhofer.iosb.eskavi.model.implementation.ModuleImp;
import de.frauenhofer.iosb.eskavi.model.implementation.ProtocolType;
import de.frauenhofer.iosb.eskavi.model.implementation.Scope;
import de.frauenhofer.iosb.eskavi.model.user.User;

import java.util.Collection;

public class Endpoint extends ModuleImp {
  private ProtocolType protocolType;

  public Endpoint(long implementationId, User author, String name, Scope scope, ProtocolType protocolType) {
    super(implementationId, author, name, scope);
    this.protocolType = protocolType;
  }

  @Override
  public boolean checkCompatibleEndpoint(Endpoint endpoint) {
    return endpoint.equals(this) || !endpoint.getProtocolType().checkCompatibility(protocolType);
  }

  @Override
  public boolean checkCompatibleSerializer(Serializer serializer) {
    return serializer.getProtocolType().checkCompatibility(protocolType);
  }

  @Override
  public boolean checkCompatibleDeserializer(Deserializer deserializer) {
    return deserializer.getProtocolType().checkCompatibility(protocolType);
  }

  @Override
  public boolean isCompatible(Collection<ImmutableModuleImp> usedImpCollection, Configuration instanceConfiguration) {
    for (ImmutableModuleImp usedImp : usedImpCollection) {
      if (!usedImp.checkCompatibleEndpoint(this)) {
        return false;
      }
    }
    for (ImmutableModuleImp usedImp : instanceConfiguration.getDependentModuleImps()) {
      if (!usedImp.checkCompatibleEndpoint(this)) {
        return false;
      }
    }
    return instanceConfiguration.checkCompatible();
  }

  public ProtocolType getProtocolType() {
    return protocolType;
  }

  public void setProtocolType(ProtocolType protocolType) {
    this.protocolType = protocolType;
  }
}
