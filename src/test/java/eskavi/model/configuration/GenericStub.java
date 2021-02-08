package eskavi.model.configuration;

import eskavi.model.implementation.ImmutableGenericImp;
import eskavi.model.implementation.ImplementationScope;
import eskavi.model.implementation.Scope;
import eskavi.model.user.ImmutableUser;

import java.util.Collection;
import java.util.Objects;

public class GenericStub implements ImmutableGenericImp {
    private String name;

    public GenericStub(String name) {
        this.name = name;
    }

    @Override
    public long getImplementationId() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public ImmutableUser getAuthor() {
        return null;
    }

    @Override
    public Scope getScope() {
        return null;
    }

    @Override
    public Collection<ImmutableUser> getUsers() {
        return null;
    }

    @Override
    public boolean isSubscribed(ImmutableUser user) {
        return false;
    }

    @Override
    public ImplementationScope getImplementationScope() {
        return null;
    }

    @Override
    public boolean checkCompatibility(ImmutableGenericImp other) {
        return other.equals(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericStub that = (GenericStub) o;
        return Objects.equals(name, that.name);
    }
}
