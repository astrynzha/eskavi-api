package eskavi.model.implementation;

import eskavi.model.user.ImmutableUser;
import eskavi.model.user.User;

public abstract class Implementation implements ImmutableImplementation {
    private long implementationId;
    private User author;
    private String name;
    private Scope scope;

    // TODO: where do ID's come from?
    protected Implementation(long implementationId, User author, String name, Scope scope) {
        this.implementationId = implementationId;
        this.author = author;
        this.name = name;
        scope.subscribe(author);
        this.scope = scope;
    }

    public void subscribe(User user) {
        if (isSubscribed(user)) {
            return;
        }
        scope.subscribe(user);
        user.subscribe(this);
    }

    public void unsubscribe(User user) {
        if (!isSubscribed(user)) {
            return;
        }
        scope.unsubscribe(user);
        user.unsubscribe(this);
    }

    @Override
    public long getImplementationId() {
        return implementationId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ImmutableUser getAuthor() {
        return (ImmutableUser) author;
    }

    @Override
    public Scope getScope() {
        return scope;
    }

    @Override
    public boolean isSubscribed(ImmutableUser user) {
        if (!(user instanceof User)) { // TODO: mehh instanceof. Is it possible to do without it here?
            return false;
        }
        return scope.isSubscribed((User) user);
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }
}
