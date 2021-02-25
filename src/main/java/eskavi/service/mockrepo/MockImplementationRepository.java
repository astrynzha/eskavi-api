//package eskavi.service.mockrepo;
//
//import eskavi.model.implementation.Implementation;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//@Component
//public class MockImplementationRepository {
//    Map<Long, Implementation> repo;
//
//    public MockImplementationRepository() {
//        this.repo = new HashMap<>();
//    }
//
//    public Iterable<Implementation> findAll() {
//        return repo.values();
//    }
//
//    public Optional<Implementation> findById(Long id) {
//        return Optional.ofNullable(repo.get(id));
//    }
//
//    public Implementation save(Implementation imp) {
//        repo.put(imp.getImplementationId(), imp);
//        return imp;
//    }
//
//    public void delete(Implementation imp) {
//        repo.remove(imp.getImplementationId());
//    }
//}
