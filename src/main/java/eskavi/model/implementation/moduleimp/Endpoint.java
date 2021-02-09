package eskavi.model.implementation.moduleimp;

import eskavi.model.configuration.Configuration;
import eskavi.model.implementation.*;
import eskavi.model.user.User;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

@Entity
public class Endpoint extends ModuleImp {
    @OneToOne
    private ProtocolType protocolType;

    public Endpoint() {
    }

    public Endpoint(long implementationId, User author, String name,
                    ImplementationScope impScope, Configuration templateConfiguration, ProtocolType protocolType) {
        super(implementationId, author, name, impScope, templateConfiguration);
        this.protocolType = protocolType;
    }

    public ProtocolType getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(ProtocolType protocolType) {
        this.protocolType = protocolType;
    }

    @Override
    public boolean isCompatible(Collection<ImmutableModuleImp> usedImpCollection) {
        for (ImmutableModuleImp usedImp : usedImpCollection) {
            if (!usedImp.checkCompatibleEndpoint(this)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkCompatibleEndpoint(Endpoint endpoint) {
        return endpoint.equals(this) || !endpoint.getProtocolType().checkCompatibility(protocolType);
    }

    @Override
    public boolean checkCompatibleSerializer(Serializer serializer) {
        return serializer.getProtocolType().checkCompatibility(protocolType);
    }

    @Override
    public boolean checkCompatibleDeserializer(Deserializer deserializer) {
        return deserializer.getProtocolType().checkCompatibility(protocolType);
    }

    @Override
    public HashSet<ImmutableGenericImp> getGenerics() {
        return new HashSet<>(Collections.singletonList(protocolType));
    }

    @Override
    public boolean isValid() {
        return super.isValid() && protocolType != null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), protocolType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Endpoint endpoint = (Endpoint) o;
        return Objects.equals(protocolType, endpoint.protocolType);
    }

    @Override
    public String toString() {
        return "Endpoint" + super.toString() +
                ", protocolType=" + protocolType +
                "}";
    }
}
