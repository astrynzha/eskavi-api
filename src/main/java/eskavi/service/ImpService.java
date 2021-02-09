package eskavi.service;

import eskavi.model.implementation.*;
import eskavi.model.user.ImmutableUser;
import eskavi.model.user.User;
import eskavi.repository.ImplementationRepository;
import eskavi.repository.UserRepository;
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
    private final UserRepository userRepository;

    public ImpService(ImplementationRepository impRepository, UserRepository userRepository) {
        this.impRepository = impRepository;
        this.userRepository = userRepository; // TODO: ausreichend, um eine userRepositry von springboot zu kriegen?
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

    /**
     * @param mi implementation that the module developer has built in frontend
     * @param caller module developer that wants to add an implementation
     */
    public void addImplementation(Implementation mi, User caller) {
        if (!mi.isValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        mi.setAuthor(caller);
        if (mi.getImplementationScope().equals(ImplementationScope.SHARED)) {
            try {
                updateScope(caller, mi);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("this should never happen, scope is SHARED " +
                        "and thus subscribe is possible");
            }
            // moduleImp is persisted inside updateScope
        } else {
            impRepository.save(mi);
        }
    }

    //TODO who checks if the initiator is the author of this implementation? Controller?
    public void addUser(long implementationId, ImmutableUser user) throws IllegalAccessException {
        Optional<Implementation> optionalImplementation = impRepository.findById(implementationId);
        if (optionalImplementation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Implementation imp = optionalImplementation.get();
        if (!imp.getImplementationScope().equals(ImplementationScope.SHARED)) {
            throw new IllegalAccessException("ImplementationScope is not SHARED");
        }
        updateScope(getMutableUser(user), getMutableImp(imp)); // TODO discuss how to handle exceptions on this example
    }

    //TODO check subscribe/unsubscribe
    public void removeUser(long implementationId, ImmutableUser immutableUser) throws IllegalAccessException {
        Optional<Implementation> optionalImplementation = impRepository.findById(implementationId);
        Optional<User> optionalUser = userRepository.findById(immutableUser.getEmailAddress());
        if (optionalImplementation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (optionalUser.isEmpty()) {
            throw new IllegalAccessException("User is not in the database");
        }
        Implementation imp = optionalImplementation.get();
        User user = optionalUser.get();
        if (imp.getImplementationScope().equals(ImplementationScope.SHARED)) {
            throw new IllegalAccessException("ImplementationScope is not SHARED");
        }
        imp.unsubscribe(user);
        user.unsubscribe(imp);
        impRepository.save(imp);
        userRepository.save(user);
    }

    //TODO wieso nicht nur id?
    public Collection<ImmutableUser> getUsers(ImmutableImplementation mi) {
        Optional<Implementation> optionalImplementation = impRepository.findById(mi.getImplementationId());
        if (optionalImplementation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Implementation imp = optionalImplementation.get();
        return imp.getUsers();
    }

    // TODO: 1. darf nur der author eine Implementation editieren?
    //  2. Is unchecked exception RespoinseStatusException OK here?
    //  3. Check that the author is subscribed when the scope is changed!!
    public void updateImplementation(ImmutableImplementation mi, User user) throws IllegalAccessException {
        Optional<Implementation> optionalImplementation = impRepository.findById(mi.getImplementationId());
        if (optionalImplementation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Implementation imp = optionalImplementation.get();
        if (!imp.getAuthor().equals(user)) {
            throw new IllegalAccessException("This user cannot update the implementation, he is not it's author");
        }
        // TODO will spring automatically update by id?
        impRepository.save(getMutableImp(mi));
    }

    public void removeImplementation(ImmutableImplementation mi, User user) throws IllegalAccessException {
        Optional<Implementation> optionalImplementation = impRepository.findById(mi.getImplementationId());
        if (optionalImplementation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Implementation imp = optionalImplementation.get();
        // TODO is needed here?
        if (!imp.getAuthor().equals(user)) {
            throw new IllegalAccessException("This user cannot remove the implementation, he is not it's author");
        }
        impRepository.delete(imp);
    }

    //TODO 1. wieso nicht nur die id?
    //  tut die Methode das gedachte?
    private void updateScope(User user, Implementation imp) throws IllegalAccessException {
        // need here both subscribe calls, because in addModuleImp(), it might happen that
        // the MI is already subscribed to the author but the user is not to the MI
        imp.subscribe(user);
        user.subscribe(imp);
        userRepository.save(user);
        impRepository.save(imp);
    }

    private ModuleImp getMutableImp(ImmutableImplementation mi) throws IllegalAccessException {
        // TODO: something else?
        if (!(mi instanceof ModuleImp)) {
            throw new IllegalAccessException("ImmutableImplementation is not an instance of ModuleImp!");
        }
        return (ModuleImp) mi;
    }

    private User getMutableUser(ImmutableUser user) throws IllegalAccessException {
        if (!(user instanceof User)) {
            throw new IllegalAccessException("ImmutableUser is not an instance of User!");
        }
        return (User) user;
    }
}

