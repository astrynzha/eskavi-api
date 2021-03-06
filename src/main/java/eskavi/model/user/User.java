package eskavi.model.user;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import eskavi.model.implementation.ImmutableImplementation;
import eskavi.model.implementation.Implementation;
import eskavi.model.implementation.ImplementationScope;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * This class represents a user of the ESKAVI web app.
 *
 * @author Niv Adam
 * @version 1.0.0
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "emailAddress")
public class User implements ImmutableUser {
    @Id
    private String emailAddress;
    private String hashedPassword;
    @Enumerated(EnumType.STRING)
    private SecurityQuestion securityQuestion;
    private String securityAnswer;
    @Enumerated(EnumType.STRING)
    private UserLevel userLevel;
    @ManyToMany
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

    //TODO what are good default values here?
    public User(String emailAddress, String hashedPassword) {
        this.emailAddress = emailAddress;
        this.hashedPassword = hashedPassword;
        this.userLevel = UserLevel.BASIC_USER;
        this.securityAnswer = "";
        this.securityQuestion = SecurityQuestion.MAIDEN_NAME;
        this.implementations = new HashSet<>();
    }

    public User() {

    }

    /**
     * Subscribes a user to a {@link Implementation} and thus gives him access to it.
     *
     * @param mi {@link Implementation} to subscribe to
     */
    public void subscribe(Implementation mi) throws IllegalAccessException {
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
    public void unsubscribe(Implementation mi) throws IllegalAccessException {
        if (!this.implementations.contains(mi)) {
            return;
        }
        this.implementations.remove(mi);
        mi.unsubscribe(this);
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
    public boolean hasAccess(ImmutableImplementation mi) {
        return mi.getImplementationScope() == ImplementationScope.PUBLIC || mi.getAuthor().equals(this)
                ||this.implementations.contains(mi);
    }

    @Override
    public Collection<ImmutableImplementation> getSubscribed() {
        return new HashSet<>(this.implementations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailAddress, hashedPassword, securityQuestion, securityAnswer, userLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(emailAddress, user.emailAddress) && Objects.equals(hashedPassword, user.hashedPassword) &&
                securityQuestion == user.securityQuestion && Objects.equals(securityAnswer, user.securityAnswer) && userLevel == user.userLevel;
    }

    @Override
    public String toString() {
        return "User{" +
                "emailAddress='" + emailAddress + '\'' +
                ", hashedPassword='" + hashedPassword + '\'' +
                ", securityQuestion=" + securityQuestion +
                ", securityAnswer='" + securityAnswer + '\'' +
                ", userLevel=" + userLevel +
                //", implementations=" + implementations +
                '}';
    }
}
