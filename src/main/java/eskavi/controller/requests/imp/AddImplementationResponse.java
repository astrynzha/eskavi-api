package eskavi.controller.requests.imp;

import eskavi.model.implementation.ImmutableImplementation;

public class AddImplementationResponse {
    private ImmutableImplementation implementation;

    public AddImplementationResponse(ImmutableImplementation implementation) {
        this.implementation = implementation;
    }

    public ImmutableImplementation getImplementation() {
        return implementation;
    }

    public void setImplementation(ImmutableImplementation implementation) {
        this.implementation = implementation;
    }
}
