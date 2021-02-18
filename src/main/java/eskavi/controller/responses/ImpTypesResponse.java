package eskavi.controller.responses;

import eskavi.model.implementation.ImpType;

import java.util.Collection;

public class ImpTypesResponse {
    private Collection<ImpType> types;

    public ImpTypesResponse(Collection<ImpType> types) {
        this.types = types;
    }

    public Collection<ImpType> getTypes() {
        return types;
    }

    public void setTypes(Collection<ImpType> types) {
        this.types = types;
    }
}
