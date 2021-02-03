package eskavi.repository;

import eskavi.model.implementation.Implementation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImpRepository extends CrudRepository<Implementation, Long> {
}