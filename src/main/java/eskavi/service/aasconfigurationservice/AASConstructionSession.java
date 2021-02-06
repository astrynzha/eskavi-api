package eskavi.service.aasconfigurationservice;

import eskavi.model.configuration.Configuration;
import eskavi.model.implementation.ModuleInstance;
import eskavi.model.user.User;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class AASConstructionSession {
    private long sessionId;
    private User owner;
    private Collection<ModuleInstance> miList;

    public AASConstructionSession(long sessionId, User owner) {
        this.sessionId = sessionId;
        this.owner = owner;
        this.miList = new LinkedList<>();
    }

    public long getSessionId() {
        return sessionId;
    }

    public void addModuleInstance(ModuleInstance moduleInstance) {
        miList.add(moduleInstance);
    }

    public void updateInstanceConfiguration(long moduleId, Configuration updateConfig) {
        // TODO
    }

    public Configuration getConfiguration(long moduleId) {
        for (ModuleInstance mi : miList) {
            if (moduleId == mi.getModuleImp().getImplementationId()) {
                return mi.getInstanceConfiguration();
            }
        }
        return null;
    }
}
