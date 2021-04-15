package eskavi.controller.responses.aas;

import eskavi.model.implementation.ImmutableImplementation;

import java.util.Collection;

public class GetImpsResponse {
    private Collection<ImmutableImplementation> implementations;

    public GetImpsResponse(Collection<ImmutableImplementation> imps) {
        this.implementations = imps;
    }

    public Collection<ImmutableImplementation> getImplementations() {
        return implementations;
    }

    public void setImplementations(Collection<ImmutableImplementation> implementations) {
        this.implementations = implementations;
    }
}
