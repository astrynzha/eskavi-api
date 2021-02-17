package eskavi.controller.requests.aas;

import eskavi.model.configuration.Configuration;

public class UpdateConfigurationRequest {
    private Configuration configuration;
    private long impId;
    private long sessionId;

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public long getImpId() {
        return impId;
    }

    public void setImpId(long impId) {
        this.impId = impId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }
}
