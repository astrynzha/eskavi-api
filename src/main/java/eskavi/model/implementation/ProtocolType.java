package eskavi.model.implementation;

import eskavi.model.user.User;

import javax.persistence.Entity;

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
