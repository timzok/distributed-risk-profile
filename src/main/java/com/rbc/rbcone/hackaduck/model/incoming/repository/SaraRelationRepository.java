package com.rbc.rbcone.hackaduck.model.incoming.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.rbc.rbcone.hackaduck.model.incoming.SaraRelation;


public interface SaraRelationRepository extends CrudRepository<SaraRelation, String>{

	@Query(nativeQuery=true, value="SELECT 1, count(0) FROM SARA_RELATION")
	Object[] findRegionLevelRelations();
		
}
