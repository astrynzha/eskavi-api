package eskavi.model.implementation.moduleimp;

import eskavi.model.implementation.ImmutableModuleImp;
import eskavi.model.implementation.ImplementationScope;
import eskavi.model.implementation.ModuleImp;
import eskavi.model.user.User;

import java.util.Collection;

public class PersistenceManager extends ModuleImp {
    public PersistenceManager() {
    }

    public PersistenceManager(long implementationId, User author, String name, ImplementationScope impScope) {
        super(implementationId, author, name, impScope);
    }

    @Override
    public boolean isCompatible(Collection<ImmutableModuleImp> usedImpCollection) {
        for (ImmutableModuleImp usedImp : usedImpCollection) {
            if (!usedImp.checkCompatiblePersistenceManager(this)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkCompatiblePersistenceManager(PersistenceManager persistenceManager) {
        return persistenceManager.equals(this);
    }
}
