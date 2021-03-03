package eskavi.model.implementation.moduleimp;

import eskavi.model.configuration.Configuration;
import eskavi.model.implementation.ImplementationScope;
import eskavi.model.implementation.ModuleImp;
import eskavi.model.user.User;

import javax.persistence.Entity;

/**
 * This class inherits the abstract class ModuleImp and represents an environment section of the final java class,
 * created by the ESKAVI tool.
 *
 * @author Andrii Strynzha
 * @version 1.0
 */
@Entity
public class Environment extends ModuleImp {
    public Environment() {
    }

    public Environment(long implementationId, User author, String name, ImplementationScope impScope,
                       Configuration templateConfiguration) {
        super(implementationId, author, name, impScope, templateConfiguration);
    }

    @Override
    public String toString() {
        return "Environment" + super.toString();
    }
}
