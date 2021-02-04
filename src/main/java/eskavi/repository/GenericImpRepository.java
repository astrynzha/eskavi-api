package eskavi.repository;

import eskavi.model.implementation.GenericImp;

import javax.transaction.Transactional;

@Transactional
public interface GenericImpRepository extends ImpBaseRepository<GenericImp> {
}
