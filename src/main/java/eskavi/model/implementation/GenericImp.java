package eskavi.model.implementation;

import eskavi.model.user.User;

import javax.persistence.Entity;

/**
 * This abstract class inherits the class Implementation and implements the interface ImmutableGenericImp.
 * This is a representation of all classes that are used as generics by some MIs. GenericImps are used to
 * check the compatibility of selected MIs and cannot be part of an AAS.
 *
 * @author Andrii Strynzha, David Kaufmann, Maximilian Georg
 * @version 1.0.0
 */
@Entity
public abstract class GenericImp extends Implementation implements ImmutableGenericImp {

    public GenericImp() {
    }

    public GenericImp(long implementationId, User author, String name, ImplementationScope impScope) {
        super(implementationId, author, name, impScope);
    }

    @Override
    public abstract boolean checkCompatibility(ImmutableGenericImp other);
}
