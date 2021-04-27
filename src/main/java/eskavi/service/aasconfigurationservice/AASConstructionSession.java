package eskavi.service.aasconfigurationservice;

import eskavi.model.configuration.Configuration;
import eskavi.model.implementation.ImmutableImplementation;
import eskavi.model.implementation.ImmutableModuleImp;
import eskavi.model.implementation.ModuleInstance;
import eskavi.model.user.User;
import eskavi.util.JavaClassConstants;
import eskavi.util.JavaClassGenerator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AASConstructionSession {

    private long sessionId;
    private User owner;
    private ConcurrentMap<Long, ModuleInstance> miMap;
    private List<String> registryList;

    public AASConstructionSession(long sessionId, User owner) {
        this.sessionId = sessionId;
        this.owner = owner;
        this.miMap = new ConcurrentHashMap<>();
        this.registryList = new LinkedList<>();
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
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
        if (hasCircularRequirements(mi, updateConfig)) {
            throw new IllegalStateException("your selection leads to circular dependencies of selected Instances");
        }
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

    private void appendDeclarations(StringBuilder codeBuilder) {
        List<ModuleInstance> alreadyAdded = new ArrayList<>();
        for (ModuleInstance instance : this.miMap.values()) {
            if (!alreadyAdded.contains(instance)) {
                appendDeclaration(instance, codeBuilder, alreadyAdded);
            }
        }
    }

    private void appendDeclaration(ModuleInstance instance, StringBuilder codeBuilder, List<ModuleInstance> alreadyUsed) {
        for (ImmutableModuleImp required : instance.getInstanceConfiguration().getRequiredInstances()) {
            appendDeclaration(miMap.get(required.getImplementationId()), codeBuilder, alreadyUsed);
        }
        codeBuilder.append(instance.getModuleImp().getName() + " " +
                        instance.getModuleImp().getName().toLowerCase() + "=" +
                        instance.resolveConfiguration());
        alreadyUsed.add(instance);
    }

    private void appendAASContent(StringBuilder codeBuilder) {
        miMap.values().forEach(mi -> codeBuilder.append(
                //TODO make classname properly
                "." + StringUtils.uncapitalize(mi.getModuleImp().getClass().getSimpleName()) + "(" +
                        mi.getModuleImp().getName().toLowerCase() + ")"
        ));
    }

    public File generateJavaClass() {
        if (this.isValid()) {
            StringBuilder codeBuilder = new StringBuilder();
            codeBuilder.append(JavaClassConstants.getClassStart());

            appendDeclarations(codeBuilder);

            codeBuilder.append(JavaClassConstants.getAasBuilderStart());

            appendAASContent(codeBuilder);

            codeBuilder.append(JavaClassConstants.getAasBuilderEnd());

            registryList.forEach(registry -> codeBuilder.append(
                    JavaClassConstants.getRegisterStart() + registry + JavaClassConstants.getRegisterEnd()
            ));

            codeBuilder.append(JavaClassConstants.getClassEnd());
            return JavaClassGenerator.generateClassFile(codeBuilder.toString());
        } else {
            //TODO what should we throw here
            throw new IllegalArgumentException("The Session has invalid Java Syntax");
        }
    }

    private boolean isValid() {
        List<ImmutableModuleImp> others = new ArrayList<>();
        for (ModuleInstance instance : miMap.values()) {
            others.add(instance.getModuleImp());
        }

        for (ModuleInstance instance : miMap.values()) {
            if (!instance.isCompatible(others) || hasCircularRequirements(instance, instance.getInstanceConfiguration())) {
                return false;
            }
        }
        return true;
    }

    public boolean hasCircularRequirements(ModuleInstance instance, Configuration configuration) {
        for (ImmutableModuleImp imp: configuration.getRequiredInstances()) {
            if (instance.getModuleImp().equals(imp)) return true;
        }

        //continue recursion
        for (ImmutableModuleImp required : configuration.getRequiredInstances()) {
            long id = required.getImplementationId();
            if (hasCircularRequirements(instance, miMap.get(id).getInstanceConfiguration())) {
                return true;
            }
        }
        return false;
    }

    public List<ImmutableModuleImp> getImps() {
        List<ImmutableModuleImp> result = new ArrayList<>();
        for (ModuleInstance instance : miMap.values()) {
            result.add(instance.getModuleImp());
        }
        return result;
    }
}
