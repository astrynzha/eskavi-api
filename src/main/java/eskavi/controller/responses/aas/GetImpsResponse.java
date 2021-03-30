package eskavi.controller.responses.aas;

import eskavi.model.implementation.ImmutableImplementation;

import java.util.Collection;

public class GetImpsResponse {
    private Collection<ImmutableImplementation> implementations;

    public GetImpsResponse(Collection<ImmutableImplementation> imps) {
        this.implementations = imps;
    }
}
