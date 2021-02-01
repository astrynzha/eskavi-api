package eskavi.model.user;

import eskavi.model.implementation.ImmutableImplementation;

import java.util.Collection;

/**
 * This interface expects getters, a User object must offer. Instances of this type help prevent accidental
 * changes of the object and thus offer a safe way to get data from a user.
 *
 * @author Niv Adam
 * @version 1.0.0
 */
public interface ImmutableUser {
    /**
     * Gets the user level of the user.
     *
     * @return user level
     */
    public UserLevel getUserLevel();

    /**
     * Gets the user's email address.
     *
     * @return email address
     */
    public String getEmailAddress();

    /**
     * Gets the user's hashed password.
     *
     * @return hashed user password
     */
    public String getPassword();

    /**
     * Gets the {@link SecurityQuestion}, the user has chosen to recover his password with.
     *
     * @return user's security question
     */
    public SecurityQuestion getSecurityQuestion();

    /**
     * Gets the answer to the user's {@link SecurityQuestion}.
     *
     * @return answer to security question
     */
    public String getSecurityAnswer();

    /**
     * Checks if user has access to the passed {@link ImmutableImplementation}
     *
     * @param mi {@link ImmutableImplementation} to be checked
     * @return true if accessible by user, false if not
     */
    public boolean isSubscribedTo(ImmutableImplementation mi);

    /**
     * Gets all {@link ImmutableImplementation} the user has access to.
     *
     * @return Collection of {@link ImmutableImplementation}
     */
    public Collection<ImmutableImplementation> getSubscribed();
}
