package com.rbc.rbcone.hackaduck.model.incoming.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.rbc.rbcone.hackaduck.model.incoming.SaraPeps;

public interface SaraPepsRepository extends CrudRepository<SaraPeps, String> {  

	@Query(nativeQuery=true, value="select p.* from sara_peps p where p.relation_id in (select r.relation_id from sara_legal_fund f, sara_entity e, sara_relation r where f.id = ? and r.lf_id = f.id and r.bp_id = e.id and e.residence_code = ? and r.rad = ?)")
	List<SaraPeps> findPepsByFundAndCountryAndRiskCategory(String aFundId, String aCountryCode, String aRiskCategory);
	
}
