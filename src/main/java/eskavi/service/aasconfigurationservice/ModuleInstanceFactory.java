package eskavi.service.aasconfigurationservice;

import eskavi.model.implementation.ModuleImp;
import eskavi.model.implementation.ModuleInstance;

/**
 * This is a utility class offering a factory method to create {@link ModuleInstance} based on a passed {@link ModuleImp}.
 *
 * @author Niv Adam
 */
public final class ModuleInstanceFactory {
    private ModuleInstanceFactory() {
        throw new AssertionError("Instantiating utility class");
    }

    /**
     * Factory method for {@link ModuleInstance}.
     *
     * @param mi the {@link ModuleImp} the instance should be based on
     * @return the instance
     */
    public static ModuleInstance build(ModuleImp mi) {
        return new ModuleInstance(mi);
    }
}
