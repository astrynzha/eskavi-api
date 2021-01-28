package de.frauenhofer.iosb.eskavi.model.implementation.moduleimp;

import de.frauenhofer.iosb.eskavi.model.implementation.ModuleImp;
import de.frauenhofer.iosb.eskavi.model.implementation.Scope;
import de.frauenhofer.iosb.eskavi.model.user.User;

public class PersistenceManager extends ModuleImp {
  public PersistenceManager(long implementationId, User author, String name, Scope scope) {
    super(implementationId, author, name, scope);
  }

  @Override
  public boolean checkCompatiblePersistenceManager(PersistenceManager persistenceManager) {
    return persistenceManager.equals(this);
  }
}
