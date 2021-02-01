package eskavi.model.implementation.moduleimp;

import eskavi.model.implementation.ModuleImp;
import eskavi.model.implementation.Scope;
import eskavi.model.user.User;

public class PersistenceManager extends ModuleImp {
    public PersistenceManager(long implementationId, User author, String name, Scope scope) {
        super(implementationId, author, name, scope);
    }

    @Override
    public boolean checkCompatiblePersistenceManager(PersistenceManager persistenceManager) {
        return persistenceManager.equals(this);
    }
}
