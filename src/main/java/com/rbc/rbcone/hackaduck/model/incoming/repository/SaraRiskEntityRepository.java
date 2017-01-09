package com.rbc.rbcone.hackaduck.model.incoming.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.rbc.rbcone.hackaduck.model.incoming.SaraEntityDB;

public interface SaraRiskEntityRepository  extends CrudRepository<SaraEntityDB, String> {

	@Query(nativeQuery=true,value="select e.*, r.relation_id,f.id as saraFundId,r.rad from sara_legal_fund f, sara_entity e, sara_relation r where  r.lf_id = f.id and r.bp_id = e.id")
	Object[] findAllSaraEntities();
	
}
