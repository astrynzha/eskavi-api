package eskavi.controller.requests.imp;

public class RemoveUserRequest {
    private long implementationId;
    private String userId;

    public long getImplementationId() {
        return implementationId;
    }

    public void setImplementationId(long implementationId) {
        this.implementationId = implementationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
