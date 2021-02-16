package eskavi.controller.requests.user;

import eskavi.model.user.UserLevel;

public class SetUserLevelRequest {
    private String email;

    public UserLevel getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(UserLevel userLevel) {
        this.userLevel = userLevel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private UserLevel userLevel;
}
