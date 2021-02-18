package eskavi.repository;

import eskavi.model.implementation.ImmutableImplementation;
import eskavi.model.implementation.Implementation;
import eskavi.model.implementation.ImplementationScope;
import eskavi.model.user.ImmutableUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ImplementationRepository extends CrudRepository<Implementation, Long> {
    Collection<ImmutableImplementation> findAllByScope_ImpScope(ImplementationScope implementationScope);
    Collection<ImmutableImplementation> findAllByAuthor(ImmutableUser user);
}
