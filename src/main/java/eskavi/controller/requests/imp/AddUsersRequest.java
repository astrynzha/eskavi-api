package eskavi.controller.requests.imp;

import java.util.Collection;

public class AddUsersRequest {
    private long impId;
    private Collection<String> userIds;
    
    public long getImpId() {
        return impId;
    }

    public void setImpId(long impId) {
        this.impId = impId;
    }

    public Collection<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(Collection<String> userIds) {
        this.userIds = userIds;
    }
}
