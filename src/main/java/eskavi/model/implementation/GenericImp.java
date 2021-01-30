package eskavi.model.implementation;

import eskavi.model.user.User;

public abstract class GenericImp extends Implementation implements ImmutableGenericImp {

    protected GenericImp(long implementationId, User author, String name, Scope scope) {
        super(implementationId, author, name, scope);
    }

    @Override
    public abstract boolean checkCompatibility(ImmutableGenericImp other);

    @Override
    public abstract boolean equals(Object obj);
}
