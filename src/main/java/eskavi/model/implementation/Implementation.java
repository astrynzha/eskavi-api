package eskavi.model.implementation;

import eskavi.model.user.ImmutableUser;
import eskavi.model.user.User;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Implementation implements ImmutableImplementation {
    @Id
    @GeneratedValue
    private long implementationId;

    private User author;
    private String name;
    private Scope scope;

    protected Implementation(long implementationId, User author, String name, ImplementationScope impScope) {
        this.implementationId = implementationId;
        this.author = author;
        this.name = name;
        this.scope = new Scope(impScope, this);
        if (impScope == ImplementationScope.SHARED) {
            try {
                scope.subscribe(author);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("This should never happen, scope is SHARED and " +
                        "the moduleImp can be subscribed to the author", e);
            }
        }
    }

    public void subscribe(User user) throws IllegalAccessException {
        if (isSubscribed(user)) {
            return;
        }
        scope.subscribe(user);
        user.subscribe(this);
    }

    public void unsubscribe(User user) throws IllegalAccessException {
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
        return (ImmutableUser) author; // TODO: should the cast be here or in view?
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
