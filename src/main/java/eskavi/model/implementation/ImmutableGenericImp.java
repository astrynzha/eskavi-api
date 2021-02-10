package eskavi.model.implementation;

/**
 * This interface inherits ImmutableImplementation and makes GenericImplementations available.
 * However, it is not possible to change the Generic Implementation via this interface.
 *
 * @author Andrii Strynzha, David Kaufmann, Maximilian Georg
 * @version 1.0.0
 */
public interface ImmutableGenericImp extends ImmutableImplementation {
    public boolean checkCompatibility(ImmutableGenericImp other);
}
