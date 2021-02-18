package eskavi.controller.responses.aas;

import eskavi.model.configuration.Configuration;

public class GetConfigurationResponse {
    private Configuration instanceConfiguration;

    public GetConfigurationResponse(Configuration configuration) {
        this.instanceConfiguration = configuration;
    }

    public Configuration getInstanceConfiguration() {
        return instanceConfiguration;
    }

    public void setInstanceConfiguration(Configuration instanceConfiguration) {
        this.instanceConfiguration = instanceConfiguration;
    }
}
