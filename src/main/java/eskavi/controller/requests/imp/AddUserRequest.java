package eskavi.controller.requests.imp;

public class AddUserRequest {
    private long impId;
    private String userId;

    public long getImpId() {
        return impId;
    }

    public void setImpId(long impId) {
        this.impId = impId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
