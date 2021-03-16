package eskavi.controller.requests.imp;

import eskavi.model.implementation.ImplementationScope;

public class UpdateImpScopeRequest {
    private ImplementationScope impScope;
    private long impId;

    public ImplementationScope getImpScope() {
        return impScope;
    }

    public void setImpScope(ImplementationScope impScope) {
        this.impScope = impScope;
    }

    public long getImpId() {
        return impId;
    }

    public void setImpId(long impId) {
        this.impId = impId;
    }
}
