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

    //TODO should the access of the caller to the Imp be checked here?
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

    // TODO
    public ImmutableModuleImp getTemplateImpCreate(Long id) {
        return null;
    }

    // TODO
    public ImmutableModuleImp getDefaultImpCreate(ImpType type) {
        //Check config to get id of ImpType
        return null;
    }

    /**
     * Scope of the implementation has to be empty. All the users have to be subscribed through addUser API call.
     *
     * @param mi       implementation that the module developer has built in frontend
     * @param callerId Id of module developer that wants to add an implementation
     */
    public void addImplementation(Implementation mi, String callerId) {
        if (!mi.isValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Optional<User> optionalCaller = userRepository.findById(callerId);
        if (optionalCaller.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        User caller = optionalCaller.get();
        mi.setAuthor(caller);
        // add author to scope if SHARED
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
    public void addUser(long implementationId, String userId, String callerId) throws IllegalAccessException {
        Optional<Implementation> optionalImplementation = impRepository.findById(implementationId);
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<User> optionalCaller = userRepository.findById(callerId);
        if (optionalImplementation.isEmpty() || optionalUser.isEmpty() || optionalCaller.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Implementation imp = optionalImplementation.get();
        User user = optionalUser.get();
        User caller = optionalCaller.get();
        if (!imp.getAuthor().equals(caller)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (!imp.getImplementationScope().equals(ImplementationScope.SHARED)) {
            throw new IllegalAccessException("ImplementationScope is not SHARED");
        }
        updateScope(user, imp); // TODO discuss how to handle exceptions on this example
    }

    //TODO check subscribe/unsubscribe
    public void removeUser(long implementationId, String userId, String callerId) throws IllegalAccessException {
        Optional<Implementation> optionalImplementation = impRepository.findById(implementationId);
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<User> optionalCaller = userRepository.findById(callerId);
        if (optionalImplementation.isEmpty() || optionalUser.isEmpty() || optionalCaller.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Implementation imp = optionalImplementation.get();
        User user = optionalUser.get();
        User caller = optionalCaller.get();
        if (!imp.getAuthor().equals(caller)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (!imp.getImplementationScope().equals(ImplementationScope.SHARED)) {
            throw new IllegalAccessException("ImplementationScope is not SHARED");
        }
        if (imp.getAuthor().equals(user)) { // cannot remove the author
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        imp.unsubscribe(user);
        user.unsubscribe(imp);
        impRepository.save(imp);
        userRepository.save(user);
    }

    //TODO wieso nicht nur id?
    public Collection<ImmutableUser> getUsers(Long implementationId) {
        Optional<Implementation> optionalImplementation = impRepository.findById(implementationId);
        if (optionalImplementation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Implementation imp = optionalImplementation.get();
        return imp.getUsers();
    }

    // TODO: 1. Is unchecked exception RespoinseStatusException OK here?
    //  2. Check that the author is subscribed when the scope is changed?!
    //  3. Soll man hier isValid auf die übergebene MI aufrufen?
    public void updateImplementation(ImmutableImplementation mi, String callerId) throws IllegalAccessException {
        Optional<User> optionalCaller = userRepository.findById(callerId);
        Optional<Implementation> optionalImplementation = impRepository.findById(mi.getImplementationId());
        if (optionalImplementation.isEmpty() || optionalCaller.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Implementation imp = optionalImplementation.get();
        User user = optionalCaller.get();
        if (!imp.getAuthor().equals(user)) {
            throw new IllegalAccessException("This caller cannot update the implementation, he is not it's author");
        }
        // TODO will spring automatically update by id?
        impRepository.save(getMutableImp(mi));
    }

    public void removeImplementation(Long implementationId, String userId) throws IllegalAccessException {
        Optional<Implementation> optionalImplementation = impRepository.findById(implementationId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalImplementation.isEmpty() || optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Implementation imp = optionalImplementation.get();
        User user = optionalUser.get();
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

