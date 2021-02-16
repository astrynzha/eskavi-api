package eskavi.controller.requests.aas;

import eskavi.model.configuration.Configuration;

public class UpdateConfigurationRequest {
    private Configuration configuration;
    private long impeId;

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public long getImpeId() {
        return impeId;
    }

    public void setImpeId(long impeId) {
        this.impeId = impeId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    private long sessionId;
}
