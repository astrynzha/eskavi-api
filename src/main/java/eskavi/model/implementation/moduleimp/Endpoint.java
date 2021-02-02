package eskavi.model.implementation.moduleimp;

import eskavi.model.implementation.*;
import eskavi.model.user.User;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class Endpoint extends ModuleImp {
    private ProtocolType protocolType;

    public Endpoint() {
    }

    public Endpoint(long implementationId, User author, String name,
                    ImplementationScope impScope, ProtocolType protocolType) {
        super(implementationId, author, name, impScope);
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
}
