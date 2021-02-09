package eskavi.model.implementation;

import eskavi.model.user.User;

import javax.persistence.Entity;

/**
 * The ProtocolType class inherits the GenericImp class. ProtocolType is one of the two generics used by ModuleImps.
 * It is used to check the compatibility of individual implementations.
 *
 * @author Andrii Strynzha, David Kaufmann, Maximilian Georg
 * @version 1.0.0
 */
@Entity
public class ProtocolType extends GenericImp {

    public ProtocolType(long implementationId, User author, String name, ImplementationScope impScope) {
        super(implementationId, author, name, impScope);
    }

    public ProtocolType() {

    }

    @Override
    public String toString() {
        return "ProtocolType" + super.toString();
    }

    //    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        return obj.getClass() == this.getClass();
//    }
}
