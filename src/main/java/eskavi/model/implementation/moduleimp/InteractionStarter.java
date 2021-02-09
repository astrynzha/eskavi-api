package eskavi.model.implementation.moduleimp;

import eskavi.model.configuration.Configuration;
import eskavi.model.implementation.ImplementationScope;
import eskavi.model.implementation.ModuleImp;
import eskavi.model.user.User;

import javax.persistence.Entity;

/**
 * This class inherits the abstract class ModuleImp and represents an MI of the type InteractionStarter.
 *
 * @author Andrii Strynzha, David Kaufmann, Maximilian Georg
 * @version 1.0.0
 */
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
