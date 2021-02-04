package eskavi.repository;

import eskavi.model.implementation.Implementation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ImpBaseRepository<T extends Implementation> extends CrudRepository<T, Long> {
}