package eskavi.model.configuration;

import eskavi.model.implementation.ImmutableGenericImp;

import java.util.Objects;

public class GenericStub implements ImmutableGenericImp {
    private String name;

    public GenericStub(String name) {
        this.name = name;
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
