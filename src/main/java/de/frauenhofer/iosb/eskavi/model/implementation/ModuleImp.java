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
  public abstract boolean checkCompatibleEndpoint(Endpoint endpoint);

  @Override
  public abstract boolean checkCompatibleSerializer(Serializer serializer);

  @Override
  public abstract boolean checkCompatibleDeserializer(Deserializer deserializer);

  @Override
  public abstract boolean checkCompatibleDispatcher(Dispatcher dispatcher);

  @Override
  public abstract boolean checkCompatibleAssetConnection(AssetConnection assetConnection);

  @Override
  public abstract boolean checkCompatibleHandler(Handler handler);

  @Override
  public abstract boolean checkCompatiblePersistenceManager(PersistenceManager persistenceManager);

  @Override
  public abstract boolean checkCompatibleInteractionStarter(InteractionStarter interactionStarter);

  @Override
  public abstract boolean isCompatible(Collection<ImmutableModuleImp> usedImpCollection, Configuration instanceConfiguration);
}
