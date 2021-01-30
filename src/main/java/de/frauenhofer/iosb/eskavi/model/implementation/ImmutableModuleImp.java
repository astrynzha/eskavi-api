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

import java.util.Collection;

public interface ImmutableModuleImp {

  public boolean isCompatible(Collection<ImmutableModuleImp> others, Configuration instanceConfiguration);

  public boolean checkCompatibleEndpoint(Endpoint endpoint);

  public boolean checkCompatibleSerializer(Serializer serializer);

  public boolean checkCompatibleDeserializer(Deserializer deserializer);

  public boolean checkCompatibleDispatcher(Dispatcher dispatcher);

  public boolean checkCompatibleAssetConnection(AssetConnection assetConnection);

  public boolean checkCompatibleHandler(Handler handler);

  public boolean checkCompatiblePersistenceManager(PersistenceManager persistenceManager);

  public boolean checkCompatibleInteractionStarter(InteractionStarter interactionStarter);
}
