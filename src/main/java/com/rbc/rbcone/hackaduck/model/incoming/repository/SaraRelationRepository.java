package com.rbc.rbcone.hackaduck.model.incoming.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.rbc.rbcone.hackaduck.model.incoming.SaraRelation;


public interface SaraRelationRepository extends CrudRepository<SaraRelation, String>{

	@Query(nativeQuery=true, value="select f.id as fundId, f.name, g.name as regionId, r.rad, sum(r.asset_Value), count(distinct e.id) from country c, region g, sara_legal_fund f, sara_entity e, sara_relation r where f.id = ? and r.lf_id = f.id and r.bp_id = e.id and e.residence_code = c.type and c.region_id = g.id group by g.id, r.rad")
	Object[] findRegionLevelRelations(String aFundId);
	
	@Query(nativeQuery=true, value="select f.id as fundId, f.name, g.name as regionId, r.rad, sum(r.asset_Value), count(distinct e.id) from country c, region g, sara_legal_fund f, sara_entity e, sara_relation r where and r.lf_id = f.id and r.bp_id = e.id and e.residence_code = c.type and c.region_id = g.id group by g.id, r.rad")
	Object[] findAllRegionLevelRelations();
	
	@Query(nativeQuery=true, value="select f.id as fundId, f.name, c.type as countryCode, c.label as countryName, r.rad, sum(r.asset_Value), count(distinct e.id) from country c, region g, sara_legal_fund f, sara_entity e, sara_relation r where f.id = ? and g.name = ? and c.region_id = g.id and r.lf_id = f.id and r.bp_id = e.id and e.residence_code = c.type group by c.type, r.rad")
	Object[] findCountryLevelRelations(String aFundId, String aRegionCode);
	
	@Query(nativeQuery=true, value="select f.id as fundId, f.name, c.type as countryCode, c.label as countryName, r.rad, sum(r.asset_Value), count(distinct e.id) from country c, region g, sara_legal_fund f, sara_entity e, sara_relation r where f.id = ? and g.name = ? and c.region_id = g.id and r.lf_id = f.id and r.bp_id = e.id and e.residence_code = c.type group by c.type, r.rad")
	Object[] findTopCountryLevelRelations(String aFundId, String aRegionCode, int aTopCount);
		
}
