package eskavi.service.mockrepo;

import eskavi.model.user.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class MockUserRepository {
    Map<String, User> repo;

    public MockUserRepository() {
        this.repo = new HashMap<>();
    }

    public Iterable<User> findAll() {
        return repo.values();
    }

    public Optional<User> findById(String email) {
        return Optional.ofNullable(repo.get(email));
    }

    public User save(User user) {
        repo.put(user.getEmailAddress(), user);
        return user;
    }

    public void delete(User user) {
        repo.remove(user.getEmailAddress());
    }

    public void deleteById(String userId) {
        repo.remove(userId);
    }
}
