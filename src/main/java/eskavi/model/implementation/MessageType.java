package eskavi.model.implementation;

import eskavi.model.user.User;

import javax.persistence.Entity;

/**
 * The class MessageType inherits the class GenericImp. MessageType is one of the two generics used by ModuleImps.
 * It is used to check the compatibility of individual implementations.
 *
 * @author Andrii Strynzha, David Kaufmann, Maximilian Georg
 * @version 1.0.0
 */
@Entity
public class MessageType extends GenericImp {

    public MessageType(long implementationId, User author, String name, ImplementationScope impScope) {
        super(implementationId, author, name, impScope);
    }

    public MessageType() {

    }

    @Override
    public String toString() {
        return "MessageType" + super.toString();
    }
}
