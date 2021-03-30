package eskavi.controller.responses.aas;

import eskavi.model.implementation.ImmutableModuleImp;
import eskavi.model.implementation.ModuleInstance;

import java.util.List;

public class GetImpsResponse {
    private List<ImmutableModuleImp> implementations;

    public GetImpsResponse(List<ImmutableModuleImp> imps) {
        this.implementations = imps;
    }
}
