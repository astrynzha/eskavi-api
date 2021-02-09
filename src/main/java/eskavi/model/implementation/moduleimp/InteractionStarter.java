package eskavi.model.implementation.moduleimp;

import eskavi.model.configuration.Configuration;
import eskavi.model.implementation.ImplementationScope;
import eskavi.model.implementation.ModuleImp;
import eskavi.model.user.User;

import javax.persistence.Entity;

@Entity
public class InteractionStarter extends ModuleImp {
    public InteractionStarter() {
    }

    public InteractionStarter(long implementationId, User author, String name, ImplementationScope impScope, Configuration templateConfiguration) {
        super(implementationId, author, name, impScope, templateConfiguration);
    }

    @Override
    public String toString() {
        return "InteractionStarter" + super.toString();
    }
}
