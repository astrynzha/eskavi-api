package eskavi.model.implementation.moduleimp;

import eskavi.model.implementation.ModuleImp;
import eskavi.model.implementation.Scope;
import eskavi.model.user.User;

public class InteractionStarter extends ModuleImp {
    public InteractionStarter(long implementationId, User author, String name, Scope scope) {
        super(implementationId, author, name, scope);
    }
}
