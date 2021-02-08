package eskavi.service.aasconfigurationservice;

import eskavi.model.implementation.ModuleImp;
import eskavi.model.implementation.ModuleInstance;

public final class ModuleInstanceFactory{
    private ModuleInstanceFactory() {
        throw new AssertionError("Instantiating utility class");
    }

    public static ModuleInstance build(ModuleImp mi) {
        return new ModuleInstance(mi, mi.getConfiguration().clone());
    }
}
