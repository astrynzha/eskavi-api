package eskavi.model.implementation;

import eskavi.model.user.ImmutableUser;

public interface ImmutableImplementation {
    public long getImplementationId();

    public String getName();

    public ImmutableUser getAuthor();

    public Scope getScope(); // TODO: or getSubscribed():List<ImmutableUser> ?

    public boolean isSubscribed(ImmutableUser user);
}
