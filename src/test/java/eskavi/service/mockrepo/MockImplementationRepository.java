package eskavi.service.mockrepo;

import eskavi.model.implementation.Implementation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MockImplementationRepository {
    Map<Long, Implementation> repo;

    public MockImplementationRepository() {
        this.repo = new HashMap<>();
    }

    public Optional<Implementation> findByID(Long id) {
        return Optional.ofNullable(repo.get(id));
    }

    public void save(Implementation imp) {
        repo.put(imp.getImplementationId(), imp);
    }

    public void delete(Implementation imp) {
        repo.remove(imp.getImplementationId());
    }
}
