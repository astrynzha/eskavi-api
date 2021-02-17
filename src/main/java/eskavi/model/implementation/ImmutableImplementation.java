package eskavi.model.implementation;

import eskavi.model.user.ImmutableUser;
import eskavi.model.user.User;

import java.util.Collection;

/**
 * This interface is used to work with implementations.
 * However, it is not possible to change the implementation via the interface.
 *
 * @author Andrii Strynzha, David Kaufmann, Maximilian Georg
 * @version 1.0.0
 */
public interface ImmutableImplementation {
    public long getId();

    public String getName();

    public ImmutableUser getAuthor();
    /**
     * @return list of all the users that are subscribed to this Implementation
     */
    public Collection<User> getSubscribed();

    public boolean isSubscribed(ImmutableUser user);

    public ImplementationScope getImplementationScope();
}
