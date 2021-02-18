package eskavi.controller.responses;

import eskavi.model.configuration.DataType;

import java.util.Collection;

public class DataTypesResponse {
    private Collection<DataType> data_types;

    public DataTypesResponse(Collection<DataType> data_types) {
        this.data_types = data_types;
    }

    public Collection<DataType> getData_types() {
        return data_types;
    }

    public void setData_types(Collection<DataType> data_types) {
        this.data_types = data_types;
    }
}
