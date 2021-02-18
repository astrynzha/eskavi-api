package eskavi.controller.responses;

import eskavi.model.implementation.ImmutableImplementation;

import java.util.Collection;

public class GetImplementationsResponse {
    private Collection<ImmutableImplementation> implementations;

    public GetImplementationsResponse(Collection<ImmutableImplementation>implementations) {
        this.implementations = implementations;
    }

    public Collection<ImmutableImplementation> getImplementations() {
        return implementations;
    }

    public void setImplementations(Collection<ImmutableImplementation> implementations) {
        this.implementations = implementations;
    }
}
