package eskavi.service;

import eskavi.model.user.ImmutableUser;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
import eskavi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserManagementService {

    final UserRepository userRepository;

    public UserManagementService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ImmutableUser createUser(String email, String hashedPassword) {
        return userRepository.save(new User(email, hashedPassword));
    }

    public ImmutableUser getUser(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void setUserLevel(ImmutableUser immutableUser, UserLevel level) throws IllegalAccessException {
        User user = getMutableUser(immutableUser);
        user.setUserLevel(level);
        userRepository.save(user);
    }

    public void setPassword(ImmutableUser immutableUser, String hashedPassword) throws IllegalAccessException {
        User user = getMutableUser(immutableUser);
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    public SecurityQuestion getSecurityQuestion(ImmutableUser user) {
        return user.getSecurityQuestion();
    }

    public boolean checkSecurityQuestion(ImmutableUser user, String answer) {
        return user.getSecurityAnswer().equals(answer);
    }

    //TODO
    public boolean checkPassword(ImmutableUser user, String hashedPassword) {
        return user.getPassword().equals(hashedPassword);
    }

    private User getMutableUser(ImmutableUser user) throws IllegalAccessException {
        if (!(user instanceof User)) {
            throw new IllegalAccessException("ImmutableUser is not an instance of User!");
        }
        return (User) user;
    }
}
