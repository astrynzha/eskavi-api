package de.frauenhofer.iosb.eskavi.model.implementation.moduleimp;

import de.frauenhofer.iosb.eskavi.model.implementation.ModuleImp;
import de.frauenhofer.iosb.eskavi.model.implementation.Scope;
import de.frauenhofer.iosb.eskavi.model.user.User;

public class InteractionStarter extends ModuleImp {
  public InteractionStarter(long implementationId, User author, String name, Scope scope) {
    super(implementationId, author, name, scope);
  }
}
