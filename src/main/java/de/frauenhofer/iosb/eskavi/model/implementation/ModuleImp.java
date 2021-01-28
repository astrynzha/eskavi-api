package de.frauenhofer.iosb.eskavi.model.implementation;

import de.frauenhofer.iosb.eskavi.model.configuration.Configuration;
import de.frauenhofer.iosb.eskavi.model.implementation.moduleimp.AssetConnection;
import de.frauenhofer.iosb.eskavi.model.implementation.moduleimp.Deserializer;
import de.frauenhofer.iosb.eskavi.model.implementation.moduleimp.Dispatcher;
import de.frauenhofer.iosb.eskavi.model.implementation.moduleimp.Endpoint;
import de.frauenhofer.iosb.eskavi.model.implementation.moduleimp.Handler;
import de.frauenhofer.iosb.eskavi.model.implementation.moduleimp.InteractionStarter;
import de.frauenhofer.iosb.eskavi.model.implementation.moduleimp.PersistenceManager;
import de.frauenhofer.iosb.eskavi.model.implementation.moduleimp.Serializer;
import de.frauenhofer.iosb.eskavi.model.user.User;

import java.util.Collection;

// TODO: is the Configuration attribute needed?
public abstract class ModuleImp extends Implementation implements ImmutableModuleImp {

  protected ModuleImp(long implementationId, User author, String name, Scope scope) {
    super(implementationId, author, name, scope);
  }

  @Override
  public boolean checkCompatibleEndpoint(Endpoint endpoint) {
    return true;
  }

  @Override
  public boolean checkCompatibleSerializer(Serializer serializer) {
    return true;
  }

  @Override
  public boolean checkCompatibleDeserializer(Deserializer deserializer) {
    return true;
  }

  @Override
  public boolean checkCompatibleDispatcher(Dispatcher dispatcher) {
    return true;
  }

  @Override
  public boolean checkCompatibleAssetConnection(AssetConnection assetConnection) {
    return true;
  }

  @Override
  public boolean checkCompatibleHandler(Handler handler) {
    return true;
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
    return true;
  }
}
