package eskavi.model.implementation;

import eskavi.model.user.User;

import javax.persistence.Entity;

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
