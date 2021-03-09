package eskavi.service;

import eskavi.model.implementation.*;
import eskavi.model.user.ImmutableUser;
import eskavi.model.user.User;
import eskavi.repository.ImplementationRepository;
import eskavi.repository.UserRepository;
import eskavi.util.Config;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * This service class contains all the functionality needed to edit and use Implementations
 *
 * @version 1.0
 * @author Andrii Strynzha, David Kaufmann, Maximilian Georg
 */
@Service
public class ImpService {
    private final ImplementationRepository impRepository;
    private final UserRepository userRepository;
    private final Config config;

    /**
     * Standard constructor
     * @param impRepository repository with implementations
     * @param userRepository repository with users
     * @param config repository with config
     */
    public ImpService(ImplementationRepository impRepository, UserRepository userRepository, Config config) {
        this.impRepository = impRepository;
        this.userRepository = userRepository;
        this.config = config;
    }

    /**
     *
     * @param impId
     * @param userId
     * @return
     */
    public ImmutableImplementation getImp(Long impId, String userId) {
        Implementation imp = impRepository.findById(impId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!user.hasAccess(imp)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return imp;
    }

    public Collection<ImmutableImplementation> getImps() {
        return StreamSupport.stream(impRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Collection<ImmutableImplementation> getImps(ImmutableUser user) {
        HashSet<ImmutableImplementation> result = new HashSet<>();
        result.addAll(user.getSubscribed());
        result.addAll(getPublic());
        result.addAll(impRepository.findAllByAuthor(user));
        return result;
    }

    public Collection<ImmutableImplementation> getImps(ImpType impType, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "could not find the user"));
        return StreamSupport.stream(impRepository.findAll().spliterator(), false)
                .filter(impType::matches)
                .filter(user::hasAccess)
                .collect(Collectors.toList());
    }


    public ImmutableImplementation getDefaultImpCreate(ImpType type) {
        long id;
        switch (type) {
            case ASSET_CONNECTION -> {
                id = config.getASSET_CONNECTION();
            }
            case DESERIALIZER -> {
                id = config.getDESERIALIZER();
            }
            case DISPATCHER -> {
                id = config.getDISPATCHER();
            }
            case ENDPOINT -> {
                id = config.getENDPOINT();
            }
            case HANDLER -> {
                id = config.getHANDLER();
            }
            case INTERACTION_STARTER -> {
                id = config.getINTERACTION_STARTER();
            }
            case PERSISTENCE_MANAGER -> {
                id = config.getPERSISTENCE_MANAGER();
            }
            case SERIALIZER -> {
                id = config.getSERIALIZER();
            }
            case PROTOCOL_TYPE -> {
                id = config.getPROTOCOL_TYPE();
            }
            case MESSAGE_TYPE -> {
                id = config.getMESSAGE_TYPE();
            }
            case ENVIRONMENT -> {
                id = config.getENVIRONMENT();
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
        return impRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    /**
     * Scope of the implementation has to be empty. All the users have to be subscribed through addUser API call.
     *
     * @param mi       implementation that the module developer has built in frontend
     * @param callerId Id of module developer that wants to add an implementation
     */
    public ImmutableImplementation addImplementation(Implementation mi, String callerId) {
        mi.setImplementationId(0);
        Optional<User> optionalCaller = userRepository.findById(callerId);
        if (optionalCaller.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        User caller = optionalCaller.get();
        mi.setAuthor(caller);
        mi.setScope(new Scope(mi.getImplementationScope()));
        if (!mi.isValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Implementation savedMi = impRepository.save(mi);
        // add author to scope if SHARED
        if (mi.getImplementationScope().equals(ImplementationScope.SHARED)) {
            try {
                updateScope(caller, savedMi);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("this should never happen, scope is SHARED " +
                        "and thus subscribe is possible");
            }
            // moduleImp is persisted inside updateScope
        }
        return savedMi;
    }

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
        updateScope(user, imp);
    }

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

    public Collection<ImmutableUser> getUsers(Long implementationId) {
        Optional<Implementation> optionalImplementation = impRepository.findById(implementationId);
        if (optionalImplementation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Implementation imp = optionalImplementation.get();
        return new HashSet<>(imp.getSubscribed());
    }

    // TODO:
    //  1. Soll man hier isValid auf die übergebene MI aufrufen?
    //  2. Check that the scope is not changed upon update
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
        impRepository.save(getMutableImp(mi));
    }

    public void removeImplementation(Long implementationId, String callerId) throws IllegalAccessException {
        Optional<Implementation> optionalImplementation = impRepository.findById(implementationId);
        Optional<User> optionalCaller = userRepository.findById(callerId);
        if (optionalImplementation.isEmpty() || optionalCaller.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Implementation imp = optionalImplementation.get();
        User caller = optionalCaller.get();
        if (!imp.getAuthor().equals(caller)) {
            throw new IllegalAccessException("This user cannot remove the implementation, he is not it's author");
        }
        //unsubscribe from all
        Collection<User> subscribers = new ArrayList();
        subscribers.addAll(imp.getSubscribed());
        for (User subscriber : subscribers) {
            subscriber.unsubscribe(imp);
            userRepository.save(subscriber);
        }
        impRepository.save(imp);
        impRepository.delete(imp);
    }

    private void updateScope(User user, Implementation imp) throws IllegalAccessException {
        // need here both subscribe calls, because in addModuleImp(), it might happen that
        // the MI is already subscribed to the author but the user is not to the MI
        imp.subscribe(user);
        user.subscribe(imp);
        userRepository.save(user);
        impRepository.save(imp);
    }

    private Implementation getMutableImp(ImmutableImplementation mi) throws IllegalAccessException {
        if (!(mi instanceof Implementation)) {
            throw new IllegalAccessException("ImmutableImplementation is not an instance of Implementation!");
        }
        return (Implementation) mi;
    }

//    private User getMutableUser(ImmutableUser user) throws IllegalAccessException {
//        if (!(user instanceof User)) {
//            throw new IllegalAccessException("ImmutableUser is not an instance of User!");
//        }
//        return (User) user;
//    }

    private Collection<ImmutableImplementation> getPublic() {
        return impRepository.findAllByScope_ImpScope(ImplementationScope.PUBLIC);
    }
}

