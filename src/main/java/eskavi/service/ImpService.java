package eskavi.service;

import eskavi.model.implementation.*;
import eskavi.model.user.ImmutableUser;
import eskavi.model.user.User;
import eskavi.repository.ImplementationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ImpService {

    private final ImplementationRepository impRepository;

    public ImpService(ImplementationRepository impRepository) {
        this.impRepository = impRepository;
    }

    public ImmutableImplementation getImp(Long id) {
        return impRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Collection<ImmutableImplementation> getImps() {
        return StreamSupport.stream(impRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Collection<ImmutableImplementation> getImps(ImmutableUser user) {
        return user.getSubscribed();
    }

    public ImmutableModuleImp getDefaultImpCreate(ImpType type) {
        //Check config to get id of ImpType
        return null;
    }

    public void addImplementation(ImmutableImplementation mi) {
        impRepository.save((Implementation) mi);
    }

    //TODO check subscribe/unsubscribe
    public void addUser(long implementationId, ImmutableUser user) throws IllegalAccessException {
        Optional<Implementation> optionalImplementation = impRepository.findById(implementationId);
        if (optionalImplementation.isPresent()) {
            Implementation imp = optionalImplementation.get();
            imp.subscribe((User) user);
            impRepository.save(imp);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    //TODO check subscribe/unsubscribe
    public void removeUser(long implementationId, ImmutableUser user) throws IllegalAccessException {
        Optional<Implementation> optionalImplementation = impRepository.findById(implementationId);
        if (optionalImplementation.isPresent()) {
            Implementation imp = optionalImplementation.get();
            imp.unsubscribe((User) user);
            impRepository.save(imp);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    //TODO wieso nicht nur id?
    public Collection<ImmutableUser> getUsers(ImmutableImplementation mi) {
        //TODO wie komme ich an die User eine mi?
        return null;
    }

    public void updateImplementation(ImmutableImplementation mi, User user) {
    }

    public void removeImplementation(ImmutableImplementation mi, User user) {
    }

    //TODO wieso nicht nur die id?
    private void updateScope(User user, ImmutableImplementation mi) {
    }

    private boolean isValid(ImmutableImplementation mi) {
        return false;
    }

    private ModuleImp getByImmutable(ImmutableImplementation mi) {
        return null;
    }
}
