package eskavi.model.implementation;

import eskavi.model.user.User;

public class MessageType extends GenericImp {

    public MessageType(long implementationId, User author, String name, ImplementationScope impScope) {
        super(implementationId, author, name, impScope);
    }

    @Override
    public boolean checkCompatibility(ImmutableGenericImp other) {
        return equals(other);
    }
}
