package eskavi.service.aasconfigurationservice;

import eskavi.model.configuration.Configuration;
import eskavi.model.implementation.*;
import eskavi.model.user.User;
import eskavi.repository.ImplementationRepository;
import eskavi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class AASConfigurationService {
    private final ImplementationRepository impRepository;
    private final AASSessionHandler sessionHandler;
    private final UserRepository userRepository;

    public AASConfigurationService(ImplementationRepository impRepository, UserRepository userRepository,
                                   AASSessionHandler sessionHandler) {
        this.impRepository = impRepository;
        this.sessionHandler = sessionHandler;
        this.userRepository = userRepository;
    }

    public long createAASConstructionSession(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return sessionHandler.createAASConstructionSession(user);
    }

    public long createAASConstructionSessionWithoutUser() {
        return sessionHandler.createAASConstructionSession(null);
    }

    public void removeAASConstructionSession(long sessionId) {
        sessionHandler.deleteAASConstructionSession(sessionId);
    }

    public void addRegistry(long sessionId, List<String> registryList) {
        AASConstructionSession session = null;
        try {
            session = sessionHandler.getAASConstructionSession(sessionId);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        session.setRegistryList(registryList);
    }

    public AASConstructionSession getSessionById(long sessionId) {
        try {
            return sessionHandler.getAASConstructionSession(sessionId);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // TODO: here we need to check if it is really the owner of the session who edits it. What's the best way to do it?
    //       probably should do in the controller. Then a getOwner(long SessionId):User Method in this class is needed
    public void addModuleInstance(long sessionId, long moduleId) {
        AASConstructionSession session = null;
        try {
            session = sessionHandler.getAASConstructionSession(sessionId);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

        Optional<Implementation> optionalImplementation = impRepository.findById(moduleId);
        if (optionalImplementation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Implementation imp = optionalImplementation.get();
        if (!(imp instanceof ModuleImp)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "implementation with given id is not a ModuleImp");
        }

        ModuleInstance instance = ModuleInstanceFactory.build((ModuleImp) imp);
        session.addModuleInstance(instance);
    }

    public Configuration getConfiguration(long sessionId, long moduleId) {
        AASConstructionSession session = null;
        try {
            session = sessionHandler.getAASConstructionSession(sessionId);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

        try {
            return session.getConfiguration(moduleId);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    public void updateConfiguration(long sessionId, Configuration configuration, long moduleId) {
        AASConstructionSession session = null;
        try {
            session = sessionHandler.getAASConstructionSession(sessionId);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

        try {
            if (configuration.checkCompatible()) {
                session.updateInstanceConfiguration(moduleId, configuration);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given Configuration is not valid. All values have to be set.");
            }
        } catch (IllegalAccessException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // TODO caller == session creator?
    public void removeModuleInstance(long sessionId, long moduleId) {
        AASConstructionSession session = null;
        try {
            session = sessionHandler.getAASConstructionSession(sessionId);
            session.removeModuleInstance(moduleId);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    public File generateJavaClass(long sessionId) {
        AASConstructionSession session = null;
        try {
            session = sessionHandler.getAASConstructionSession(sessionId);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return session.generateJavaClass();
    }

    public List<ImmutableModuleImp> getTopLevelImps(long sessionId) {
        AASConstructionSession session = null;
        try {
            session = sessionHandler.getAASConstructionSession(sessionId);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return session.getImps();
    }
}
