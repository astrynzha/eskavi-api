package eskavi.service.aasconfigurationservice;

import eskavi.model.configuration.Configuration;
import eskavi.model.implementation.Implementation;
import eskavi.model.implementation.ModuleImp;
import eskavi.model.implementation.ModuleInstance;
import eskavi.model.user.User;
import eskavi.repository.ImpRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AASConfigurationService {
    private final ImpRepository impRepository;
    private final AASSessionHandler sessionHandler;

    //TODO Autowire miFactory & sessionHanlder
    public AASConfigurationService(ImpRepository impRepository,
                                   AASSessionHandler sessionHandler) {
        this.impRepository = impRepository;
        this.sessionHandler = sessionHandler;
    }

    public long createAASConstructionSession(User user) {
        return sessionHandler.createAASConstructionSession(user);
    }

    public void removeAASConstructionSession(long sessionId) {
        sessionHandler.deleteAASConstructionSession(sessionId);
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

        return session.getConfiguration(moduleId);
    }

    public void updateConfiguration(long sessionId, Configuration configuration, long moduleId) {
        AASConstructionSession session = null;
        try {
            session = sessionHandler.getAASConstructionSession(sessionId);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

        try {
            session.updateInstanceConfiguration(moduleId, configuration);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public void removeModuleInstance(long sessionId, long moduleId) {
        AASConstructionSession session = null;
        try {
            session = sessionHandler.getAASConstructionSession(sessionId);
            session.removeModuleInstance(moduleId);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // TODO: generateJavaClass(long sessionId): File

}
