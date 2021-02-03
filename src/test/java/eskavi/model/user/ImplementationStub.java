package eskavi.model.user;

import eskavi.model.implementation.Implementation;
import eskavi.model.implementation.ImplementationScope;

public class ImplementationStub extends Implementation {

    protected ImplementationStub(long implementationId, User author, String name, ImplementationScope impScope) {
        super(implementationId, author, name, impScope);
    }
}
