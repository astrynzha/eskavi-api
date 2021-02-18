package eskavi.controller.responses.aas;

public class CreateSessionResponse {
    private long sessionId;

    public CreateSessionResponse(long sessionId) {
        this.sessionId = sessionId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }
}
