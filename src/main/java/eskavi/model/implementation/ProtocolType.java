package eskavi.model.implementation;

import eskavi.model.user.User;

public class ProtocolType extends GenericImp {

    public ProtocolType(long implementationId, User author, String name, ImplementationScope impScope) {
        super(implementationId, author, name, impScope);
    }

    @Override
    public boolean checkCompatibility(ImmutableGenericImp other) {
        return equals(other);
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        return obj.getClass() == this.getClass();
//    }
}
