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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * This service class contains all the functionality needed to edit and use Implementations
 *
 * @author Andrii Strynzha, David Kaufmann, Maximilian Georg
 * @version 1.0
 */
@Service
public class ImpService {
    private final ImplementationRepository impRepository;
    private final UserRepository userRepository;
    private final Config config;

    /**
     * Standard constructor
     *
     * @param impRepository  repository with implementations
     * @param userRepository repository with users
     * @param config         repository with config
     */
    public ImpService(ImplementationRepository impRepository, UserRepository userRepository, Config config) {
        this.impRepository = impRepository;
        this.userRepository = userRepository;
        this.config = config;
    }

    /**
     * Returns an implementation with the specified ID.
     *
     * @param impId  implementation id
     * @param userId user id
     * @return requested implementation
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


    /**
     * @return all implementations from the repository
     */
    public Collection<ImmutableImplementation> getImps() {
        return StreamSupport.stream(impRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * returns all implementation that the caller is subscribed to.
     *
     * @param caller user calls the function
     * @return collection of implementations
     */
    public Collection<ImmutableImplementation> getImps(ImmutableUser caller) {
        HashSet<ImmutableImplementation> result = new HashSet<>();
        result.addAll(caller.getSubscribed());
        result.addAll(getPublic());
        result.addAll(impRepository.findAllByAuthor(caller));
        return result;
    }

    /**
     * returns all implementations that the caller is subscribed to, filtered by the implementation type.
     *
     * @param impType  implementation Type.
     * @param callerId caller ID.
     * @return collection of implementations.
     */
    public Collection<ImmutableImplementation> getImps(ImpType impType, String callerId) {
        User user = userRepository.findById(callerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "could not find the user"));
        return StreamSupport.stream(impRepository.findAll().spliterator(), false)
                .filter(impType::matches)
                .filter(user::hasAccess)
                .collect(Collectors.toList());
    }

    /**
     * returns all implementations that the caller is subscribed to, filtered by the implementation type and there generics.
     *
     * @param impType  implementation Type.
     * @param callerId caller ID.
     * @return collection of implementations.
     */
    public Collection<ImmutableImplementation> getImps(ImpType impType, Collection<Long> generics, String callerId) {
        User user = userRepository.findById(callerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "could not find the user"));
        Set<ImmutableGenericImp> genericImps = StreamSupport.stream(impRepository.findAllById(generics).spliterator(), false)
                .filter(i -> i instanceof GenericImp)
                .map(i -> (GenericImp) i)
                .collect(Collectors.toSet());
        return StreamSupport.stream(impRepository.findAll().spliterator(), false)
                .filter(i -> filterByImpType(impType, i))
                .filter(user::hasAccess)
                .filter(i -> i instanceof ModuleImp)
                .map(i -> (ModuleImp) i)
                .filter(mi -> containsGenerics(mi, genericImps))
                .collect(Collectors.toList());
    }

    private boolean filterByImpType(ImpType impType, ImmutableImplementation mi) {
        if (impType == null) {
            return true;
        }
        return impType.matches(mi);
    }

    private boolean containsGenerics(ModuleImp mi, Collection<ImmutableGenericImp> generics) {
        return !Collections.disjoint(mi.getGenerics(), generics);
    }

    /**
     * Returns default Implementation for the provided Implementation type
     *
     * @param type implementation type
     * @return default Implementation
     */
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
     * Adds an Implementation to the system, if the implementation is completely initialized, has an empty scope or,
     * if SHARED, has the author in the scope.
     * Scope of the implementation has to be empty. All the users have to be subscribed through addUser API call.
     *
     * @param mi       implementation that the module developer has built in frontend
     * @param callerId Id of module developer that wants to add an implementation
     */
    public ImmutableImplementation addImplementation(Implementation mi, String callerId) {
        if (!mi.isValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        mi.setImplementationId(0);
        Optional<User> optionalCaller = userRepository.findById(callerId);
        if (optionalCaller.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        User caller = optionalCaller.get();
        mi.setAuthor(caller);
        mi.setScope(new Scope(mi.getImplementationScope())); // set an empty scope
        Implementation savedMi = impRepository.save(mi);

        return impRepository.findById(mi.getImplementationId())
                .orElseThrow(() -> new IllegalStateException("this should not have happened, mi was persisted"));
    }

    /**
     * Subscribes a user to the Implementation and the Implementation to the User, if the caller is the author of the
     * Implementation.
     *
     * @param implementationId Implementation ID to subscribe the user to.
     * @param userId           user ID to subscribe implementation to.
     * @param callerId         id of the caller.
     * @throws IllegalAccessException if implementation is not SHARED.
     */
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

    /**
     * Unsubscribes a user from the Implementation and the Implementation from the User, if the caller is the author
     * of the Implementation.
     *
     * @param implementationId Implementation ID to unsubscribe the user from.
     * @param userId           user ID to unsubscribe implementation from.
     * @param callerId         id of the caller.
     * @throws IllegalAccessException if implementation is not SHARED.
     */
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

    /**
     * Returns all the users that are subscribed to the specified Implementation
     *
     * @param implementationId id of the implementation
     * @return user subscribed to the implementation.
     */
    public Collection<ImmutableUser> getUsers(Long implementationId) {
        Optional<Implementation> optionalImplementation = impRepository.findById(implementationId);
        if (optionalImplementation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Implementation imp = optionalImplementation.get();
        return new HashSet<>(imp.getSubscribed());
    }

    /**
     * Used to modify the implementation.
     * Substitutes the old implementation entity in repo with the received one.
     *
     * @param mi       module implementation
     * @param callerId id of the caller
     * @throws IllegalAccessException if the caller is not the author of the mi and hence, cannot modify it.
     */
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

    /**
     * Removes an Implementation from the system, if the caller is the author.
     *
     * @param implementationId implementation id
     * @param callerId         Id the caller
     */
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

    private Collection<ImmutableImplementation> getPublic() {
        return impRepository.findAllByScope_ImpScope(ImplementationScope.PUBLIC);
    }

    public ImmutableUser getPublicUser() {
        return userRepository.findById(config.getPUBLIC_USER_ID()).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    public void updateImpScope(ImplementationScope impScope, long impId, ImmutableUser caller) {
        Implementation imp = impRepository.findById(impId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!imp.getImplementationScope().equals(impScope)) {
            if (imp.getAuthor().equals(caller)) {
                imp.setScope(new Scope(impScope));
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }
        impRepository.save(imp);
    }
}

