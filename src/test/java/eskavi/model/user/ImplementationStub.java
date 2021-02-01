package eskavi.model.user;

import eskavi.model.implementation.Implementation;
import eskavi.model.implementation.Scope;

public class ImplementationStub extends Implementation {

    protected ImplementationStub(long implementationId, User author, String name, Scope scope) {
        super(implementationId, author, name, scope);
    }
}
