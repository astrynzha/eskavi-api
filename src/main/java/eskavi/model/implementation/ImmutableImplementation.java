package eskavi.model.implementation;

import eskavi.model.user.ImmutableUser;
import eskavi.model.user.User;

import java.util.Collection;

public interface ImmutableImplementation {
    public long getImplementationId();

    public String getName();

    public ImmutableUser getAuthor();

    public Scope getScope(); // TODO: or getSubscribed():List<ImmutableUser> ?

    public Collection<ImmutableUser> getUsers();

    public boolean isSubscribed(ImmutableUser user);

    public ImplementationScope getImplementationScope();
}
