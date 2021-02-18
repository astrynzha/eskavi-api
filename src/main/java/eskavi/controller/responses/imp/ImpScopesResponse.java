package eskavi.controller.responses.imp;

import eskavi.model.implementation.ImplementationScope;

import java.util.Collection;

public class ImpScopesResponse {
    private Collection<ImplementationScope> impScopes;

    public ImpScopesResponse(Collection<ImplementationScope> impScopes) {
        this.impScopes = impScopes;
    }

    public Collection<ImplementationScope> getImpScopes() {
        return impScopes;
    }

    public void setImpScopes(Collection<ImplementationScope> impScopes) {
        this.impScopes = impScopes;
    }
}
