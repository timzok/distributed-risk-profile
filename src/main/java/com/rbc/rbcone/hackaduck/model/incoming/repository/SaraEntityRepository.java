package com.rbc.rbcone.hackaduck.model.incoming.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.rbc.rbcone.hackaduck.model.incoming.SaraEntity;

public interface SaraEntityRepository extends CrudRepository<SaraEntity, String> { 

	@Query(nativeQuery=true, value="select e.*, r.relation_id from sara_legal_fund f, sara_entity e, sara_relation r where f.id = ? and r.lf_id = f.id and r.bp_id = e.id and e.residence_code = ? and r.rad = ?")
	List<Object[]> findByFundAndDomicilationAndRiskCategory(String aFundId, String aCountryCode, String aRiskCategory);
	
}

