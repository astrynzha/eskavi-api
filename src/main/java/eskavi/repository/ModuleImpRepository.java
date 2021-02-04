package eskavi.repository;

import eskavi.model.implementation.ModuleImp;

import javax.transaction.Transactional;

@Transactional
public interface ModuleImpRepository extends ImpBaseRepository<ModuleImp>{
}
