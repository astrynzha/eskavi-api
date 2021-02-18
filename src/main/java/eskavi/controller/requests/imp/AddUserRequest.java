package eskavi.controller.requests.imp;

import java.util.List;

public class AddUserRequest {
    private long impId;
    private List<String> userIds;

    public long getImpId() {
        return impId;
    }

    public void setImpId(long impId) {
        this.impId = impId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
