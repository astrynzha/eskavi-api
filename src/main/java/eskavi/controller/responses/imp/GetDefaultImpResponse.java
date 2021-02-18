package eskavi.controller.responses.imp;

import eskavi.model.implementation.ImmutableImplementation;

public class GetDefaultImpResponse {
    private ImmutableImplementation template;

    public GetDefaultImpResponse(ImmutableImplementation template) {
        this.template = template;
    }

    public ImmutableImplementation getTemplate() {
        return template;
    }

    public void setTemplate(ImmutableImplementation template) {
        this.template = template;
    }
}
