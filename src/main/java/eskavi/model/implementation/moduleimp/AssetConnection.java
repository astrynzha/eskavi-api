package eskavi.model.implementation.moduleimp;

import eskavi.model.configuration.Configuration;
import eskavi.model.implementation.ImplementationScope;
import eskavi.model.implementation.ModuleImp;
import eskavi.model.user.User;

import javax.persistence.Entity;

/**
 * This class inherits the abstract class ModuleImp and represents an MI of the type asset connection.
 *
 * @author Andrii Strynzha, David Kaufmann, Maximilian Georg
 * @version 1.0.0
 */
@Entity
public class AssetConnection extends ModuleImp {

    /**
     * Constructs an asset connection object.
     */
    public AssetConnection() {
    }

    /**
     * Constructs an asset connection object.
     * @param implementationId id
     * @param author author
     * @param name name
     * @param impScope implementation Scope
     * @param templateConfiguration configuration of this moduleImp
     */
    public AssetConnection(long implementationId, User author, String name, ImplementationScope impScope,
                           Configuration templateConfiguration) {
        super(implementationId, author, name, impScope, templateConfiguration);
    }

    @Override
    public String toString() {
        return "AssetConnection" + super.toString();
    }
}
