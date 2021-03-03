package eskavi.controller.requests.aas;

import java.util.List;

public class AddRegistryRequest {
    private long sessionId;
    private List<String> url;

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public List<String> getUrl() {
        return url;
    }

    public void setUrl(List<String> url) {
        this.url = url;
    }
}
