package eskavi.repository;

import eskavi.model.implementation.GenericImp;
import eskavi.model.implementation.ImpType;
import eskavi.model.implementation.Implementation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ImpRepository extends CrudRepository<Implementation, Long> {
    Collection<Implementation> findByType(ImpType impType);

    Collection<Implementation> findByGeneric(GenericImp genericType);
}
