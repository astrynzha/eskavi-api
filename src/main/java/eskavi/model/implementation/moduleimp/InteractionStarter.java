package eskavi.model.implementation.moduleimp;

import eskavi.model.implementation.ImplementationScope;
import eskavi.model.implementation.ModuleImp;
import eskavi.model.implementation.Scope;
import eskavi.model.user.User;

import javax.persistence.Entity;

@Entity
public class InteractionStarter extends ModuleImp {
    public InteractionStarter() {
    }

    public InteractionStarter(long implementationId, User author, String name, ImplementationScope impScope) {
        super(implementationId, author, name, impScope);
    }
}
