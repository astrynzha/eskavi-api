package eskavi.model.configuration;

import eskavi.model.implementation.ImmutableImplementation;
import eskavi.model.user.ImmutableUser;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.UserLevel;

import java.util.Collection;

public class UserStub implements ImmutableUser {

    public UserStub() {

    }

    @Override
    public UserLevel getUserLevel() {
        return null;
    }

    @Override
    public String getEmailAddress() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public SecurityQuestion getSecurityQuestion() {
        return null;
    }

    @Override
    public String getSecurityAnswer() {
        return null;
    }

    @Override
    public boolean isSubscribedTo(ImmutableImplementation mi) {
        return false;
    }

    @Override
    public Collection<ImmutableImplementation> getSubscribed() {
        return null;
    }
}
