package de.frauenhofer.iosb.eskavi.model.user;

import de.frauenhofer.iosb.eskavi.model.implementation.ImmutableImplementation;
import de.frauenhofer.iosb.eskavi.model.implementation.Implementation;

import java.util.Collection;
import java.util.HashSet;

/**
 * This class represents a user of the ESKAVI web app.
 *
 * @author Niv Adam
 * @version 1.0.0
 */
public class User implements ImmutableUser {
    private String emailAddress;
    private String hashedPassword;
    private SecurityQuestion securityQuestion;
    private String securityAnswer;
    private UserLevel userLevel;
    private Collection<Implementation> implementations;

    /**
     * Constructs a user object.
     *
     * @param emailAddress     user email address
     * @param hashedPassword   user password (hashed)
     * @param userLevel        user access level
     * @param securityQuestion user {@link SecurityQuestion}
     * @param securityAnswer   answer to chosen {@link SecurityQuestion}
     */
    public User(String emailAddress, String hashedPassword, UserLevel userLevel, SecurityQuestion securityQuestion, String securityAnswer) {
        this.emailAddress = emailAddress;
        this.hashedPassword = hashedPassword;
        this.userLevel = userLevel;
        this.securityAnswer = securityAnswer;
        this.securityQuestion = securityQuestion;
        this.implementations = new HashSet<>();
    }

    @Override
    public UserLevel getUserLevel() {
        return this.userLevel;
    }

    /**
     * Sets the user's {@link UserLevel}.
     *
     * @param userLevel {@link UserLevel} to be set
     */
    public void setUserLevel(UserLevel userLevel) {
        this.userLevel = userLevel;
    }

    @Override
    public String getEmailAddress() {
        return this.emailAddress;
    }

    @Override
    public String getPassword() {
        return this.hashedPassword;
    }

    /**
     * Sets the user's password.
     *
     * @param hashedPassword new password to be set
     */
    public void setPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    @Override
    public SecurityQuestion getSecurityQuestion() {
        return this.securityQuestion;
    }

    @Override
    public String getSecurityAnswer() {
        return this.securityAnswer;
    }

    @Override
    public boolean isSubscribedTo(ImmutableImplementation mi) {
        //todo implement (wird hier auch gechecked wenn öffentliche imp?)
        return this.implementations.contains(mi);
    }

    @Override
    public Collection<ImmutableImplementation> getSubscribed() {
        //todo implement (werden hier auch öffentliche imps zurückgegeben?)
        return new HashSet<>(this.implementations);
    }

    /**
     * Subscribes a user to a {@link Implementation} and thus gives him access to it.
     *
     * @param mi {@link Implementation} to subscribe to
     */
    public void subscribe(Implementation mi) {
        if (this.implementations.contains(mi)) {
            return;
        }
        this.implementations.add(mi);
        mi.subscribe(this);
    }

    /**
     * Unsubsribes a user from a {@link Implementation}.
     *
     * @param mi {@link Implementation} to unsubscribe from
     */
    public void unsubscribe(Implementation mi) {
        if (!this.implementations.contains(mi)) {
            return;
        }
        this.implementations.remove(mi);
        mi.unsubscribe(this);
    }

}
