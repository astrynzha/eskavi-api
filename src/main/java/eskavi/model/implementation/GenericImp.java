package eskavi.model.implementation;

import eskavi.model.user.User;

import javax.persistence.Entity;

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
