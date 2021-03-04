package eskavi.service;

import eskavi.model.user.ImmutableUser;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
import eskavi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

//TODO all parameters as ids
@Service
public class UserManagementService {

    final UserRepository userRepository;

    public UserManagementService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // TODO other constructor of User
    public ImmutableUser createUser(String email, String hashedPassword) {
        if (userRepository.findById(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email is already registered");
        }
        return userRepository.save(new User(email, hashedPassword));
    }

    public ImmutableUser getUser(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Collection<ImmutableUser> getAllUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    // TODO who sets the admin
    public void setUserLevel(String userId, UserLevel level, String callerId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "could not find the user"));
        User caller = userRepository.findById(callerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "could not find the caller"));
        if (!caller.getUserLevel().equals(UserLevel.ADMINISTRATOR)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        user.setUserLevel(level);
        userRepository.save(user);
    }

    // TODO: caller has to be an admin or the user that wants to delete himself
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    public void setPassword(String userId, String hashedPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "could not find the user"));
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    public SecurityQuestion getSecurityQuestion(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "could not find the user"));
        return user.getSecurityQuestion();
    }

    public boolean checkSecurityQuestion(String userId, String answer) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "could not find the user"));
        return user.getSecurityAnswer().equals(answer);
    }

    public boolean checkPassword(String userId, String hashedPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "could not find the user"));
        return user.getPassword().equals(hashedPassword);
    }

//    private User getMutableUser(ImmutableUser user) throws IllegalAccessException {
//        if (!(user instanceof User)) {
//            throw new IllegalAccessException("ImmutableUser is not an instance of User!");
//        }
//        return (User) user;
//    }
}
