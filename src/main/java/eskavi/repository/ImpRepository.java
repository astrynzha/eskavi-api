package eskavi.repository;

import eskavi.model.implementation.Implementation;

import javax.transaction.Transactional;

@Transactional
public interface ImpRepository extends ImpBaseRepository<Implementation> {
}
