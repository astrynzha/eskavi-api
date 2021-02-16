package eskavi.controller.requests.aas;

public class AddModuleImpRequest {
    private long sessionId;
    private long impId;

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public long getImpId() {
        return impId;
    }

    public void setImpId(long impId) {
        this.impId = impId;
    }
}
