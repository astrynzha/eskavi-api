package eskavi.service.aasconfigurationservice;

import eskavi.model.configuration.Configuration;
import eskavi.model.implementation.ImmutableModuleImp;
import eskavi.model.implementation.ModuleInstance;
import eskavi.model.user.User;
import eskavi.util.JavaClassGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AASConstructionSession {
    private long sessionId;
    private User owner;
    private Map<Long, ModuleInstance> miMap;
    private List<String> registryList;

    public AASConstructionSession(long sessionId, User owner) {
        this.sessionId = sessionId;
        this.owner = owner;
        this.miMap = new LinkedHashMap<>();
        this.registryList = new LinkedList<>();
    }

    public long getSessionId() {
        return sessionId;
    }

    public void addModuleInstance(ModuleInstance moduleInstance) {
        miMap.put(moduleInstance.getImpId(), moduleInstance);
    }

    public void removeModuleInstance(long moduleId) throws IllegalAccessException {
        if (!miMap.containsKey(moduleId)) {
            throw new IllegalAccessException("no MI with id " + moduleId + " is found in session");
        }
        miMap.remove(moduleId);
    }

    // TODO check if the new configuration is valid
    public void updateInstanceConfiguration(long moduleId, Configuration updateConfig) throws IllegalAccessException {
        if (!miMap.containsKey(moduleId)) {
            throw new IllegalAccessException("no MI with id " + moduleId + " is found in session");
        }
        ModuleInstance mi = miMap.get(moduleId);
        mi.setInstanceConfiguration(updateConfig);
        miMap.replace(moduleId, mi);
    }

    public void setRegistryList(List<String> registryList) {
        this.registryList = registryList;
    }

    public Configuration getConfiguration(long moduleId) throws IllegalAccessException {
        if (!miMap.containsKey(moduleId)) {
            throw new IllegalAccessException("ModuleInstance " + moduleId +
                    " is not found in the constructions session " + sessionId);
        }
        return miMap.get(moduleId).getInstanceConfiguration();
    }

    public File generateJavaClass() {
        if (this.isValid()) {
            StringBuilder codeBuilder = new StringBuilder();
            String javaClassStart = "class App{ public static void main(String[] args){";
            String javaClassEnd = "}}";
            codeBuilder.append(javaClassStart);
            miMap.values().forEach(mi -> codeBuilder.append(
                    mi.getModuleImp().getName() + " " +
                            mi.getModuleImp().getName().toLowerCase() + "=" +
                            mi.resolveConfiguration()
            ));
            codeBuilder.append(javaClassEnd);
            return JavaClassGenerator.generateClassFile(codeBuilder.toString());
        } else {
            //TODO what should we throw here
            throw new IllegalArgumentException("");
        }
    }

    private boolean isValid() {
        List<ImmutableModuleImp> others = new ArrayList<>();
        for (ModuleInstance instance : miMap.values()) {
            others.add(instance.getModuleImp());
        }

        for (ModuleInstance instance : miMap.values()) {
            if (!instance.isCompatible(others)) {
                return false;
            }
        }
        return true;
    }
}
