package com.rbc.rbcone.hackaduck.model.incoming.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rbc.rbcone.hackaduck.model.incoming.CountryRiskDB;
import com.rbc.rbcone.hackaduck.model.incoming.RegionRiskDB;
import com.rbc.rbcone.hackaduck.model.incoming.SaraEntityDB;
import com.rbc.rbcone.hackaduck.model.incoming.repository.CountryRiskRepository;
import com.rbc.rbcone.hackaduck.model.incoming.repository.RegionRiskRepository;
import com.rbc.rbcone.hackaduck.model.incoming.repository.SaraEntityRepository;
import com.rbc.rbcone.hackaduck.model.incoming.repository.SaraRelationRepository;
import com.rbc.rbcone.hackaduck.model.incoming.repository.SaraRiskEntityRepository;
import com.rbc.rbcone.hackaduck.model.incoming.service.BusinessDataService;

@Service
public class BusinessDataServiceImpl implements BusinessDataService {

	@Autowired
	RegionRiskRepository regionRiskRepository;
	
	@Autowired
	CountryRiskRepository countryRiskRepository;
	
	@Autowired
	SaraRelationRepository saraRelationRepo;
	
	@Autowired
	SaraEntityRepository saraEntityRepo;
	
	@Autowired
	private SaraRiskEntityRepository sarRiskRepo;
	
	@PersistenceContext
	private EntityManager em;
		
	@Override
	public void feedBusinessDataForRegions()
	{

		List<RegionRiskDB> rsltList = new ArrayList<RegionRiskDB>();
		
		Object[] rsRows = saraRelationRepo.findAllRegionLevelRelations();
		
		for (int i=0; i<rsRows.length;i++) {
			
			RegionRiskDB rslt = new RegionRiskDB();
			Object[] rsRow = (Object[])rsRows[i];
			
			rslt.setFundId((String)rsRow[0]); //LEM
			rslt.setFundName((String)rsRow[1]); //MATETAM RATET
			rslt.setRegionId((String)rsRow[2]);
			rslt.setRad((String)rsRow[3]);
			rslt.setSumAssetValue((double)rsRow[4]);
			rslt.setCountEntity(((BigInteger)rsRow[5]).intValue());
			rsltList.add(rslt);	
		}
		
		regionRiskRepository.save(rsltList);	
	}
	
	/*select f.id as fundId, f.name, c.type as countryCode, c.label as countryName, r.rad, sum(r.asset_Value), count(distinct e.id) 
	 * from country c, region g, sara_legal_fund f, sara_entity e, sara_relation r 
	 * where c.region_id = g.id and r.lf_id = f.id and r.bp_id = e.id and e.residence_code = c.type 
	 * group by f.id,c.type, r.rad*/
	
	// ADD GROUP NAME
	@Override
	public void feedBusinessDataForSaraEntities(){
		
		List<SaraEntityDB> rsltList = new ArrayList<SaraEntityDB>();
		Object[] rsRows = sarRiskRepo.findAllSaraEntities();
		
		for (int i=0; i< rsRows.length;i++){
			SaraEntityDB saraEntity = new SaraEntityDB();
			Object[] rsRow = (Object[]) rsRows[i];
			saraEntity.setSaraEntityId((String)rsRow[0]);
			saraEntity.setName((String)rsRow[1]);
			saraEntity.setNature((String)rsRow[2]);
			saraEntity.setResidenceCode((String)rsRow[3]);
			saraEntity.setType((String)rsRow[4]);
			saraEntity.setRelationId((String)rsRow[5]);
			saraEntity.setSaraFundId((String)rsRow[6]);
			saraEntity.setRad((String)rsRow[7]);
			rsltList.add(saraEntity);
		}
		sarRiskRepo.save(rsltList);
	}
	
	@Override
	public void feedBusinessDataForCountries(){
		
		// select f.id as fundId, f.name, c.type as countryCode, c.label as countryName, r.rad,g.id, sum(r.asset_Value), count(distinct e.id) 
		// from country c, region g, sara_legal_fund f, sara_entity e, sara_relation r 
		// where f.id = ? and g.name = ? and c.region_id = g.id and r.lf_id = f.id and r.bp_id = e.id and e.residence_code = c.type group by c.type, r.rad")
		
		List<CountryRiskDB> rsltList = new ArrayList<CountryRiskDB>();
		Object[] rsRows = saraRelationRepo.findAllCountryLevelRelations();
		
		for (int i=0; i<rsRows.length;i++)
		{
			CountryRiskDB rslt = new CountryRiskDB();
			Object[] rsRow = (Object[]) rsRows[i];
			
			//System.out.println(rsRow[0]+"-"+rsRow[1]+"-"+rsRow[2]+"-"+rsRow[3]+"-"+rsRow[4]+"-"+rsRow[5]+"-"+rsRow[6]+"-"+rsRow[7]);
			
			rslt.setFundId((String)rsRow[0]);
			rslt.setFundName((String)rsRow[1]);
			rslt.setCountryCode((String)rsRow[2]);
			rslt.setCountryName((String)rsRow[3]);
			rslt.setRad((String)rsRow[4]);
			rslt.setRegionId((String)rsRow[5]);
			rslt.setSumAssetValue((double)rsRow[6]);
			rslt.setCountEntity(((BigInteger)rsRow[7]).intValue());
			rsltList.add(rslt);
		}
		
		System.out.println("FINAL SIZE:" + rsltList.size());
		countryRiskRepository.save(rsltList);
	
	}
	
	@Override
	  public List<RegionRiskDB> findRegionLevelRelationByFundId(String aFundId) {

	    TypedQuery<RegionRiskDB> query = em.createQuery("select r from RegionRiskDB r where fundId = ?1", RegionRiskDB.class);
	    query.setParameter(1, aFundId);
	    return query.getResultList();
	  }

	@Override
	public List<CountryRiskDB> findCountryRiskByFundAndRegion(String fundId, String regionCode){
		
		TypedQuery<CountryRiskDB> query = em.createQuery("select c from CountryRiskDB c where fundId = ?1 and regionId = ?2",CountryRiskDB.class);
		query.setParameter(1, fundId);
		query.setParameter(2, regionCode);
		return query.getResultList();
	}
	
	@Override
	public List<CountryRiskDB> findCountryRiskByFundAndCountry(String fundId, String countryCode){
		
		TypedQuery<CountryRiskDB> query = em.createQuery("select c from CountryRiskDB c where fundId = ?1 and countryCode = ?2",CountryRiskDB.class);
		query.setParameter(1, fundId);
		query.setParameter(2, countryCode);
		return query.getResultList();
	}
	
	@Override
	public List<CountryRiskDB> findTopXCountryRiskByFundAndRegion(String fundId, String regionCode, int topX){
	
		TypedQuery<CountryRiskDB> query = em.createQuery("select c from CountryRiskDB c where fundId = ?1 and regionId = ?2 and rad='H'  order by c.sumAssetValue desc",CountryRiskDB.class);// and rownum < ?3 order by c.sumAssetValue desc
		query.setParameter(1, fundId);
		query.setParameter(2, regionCode);
		query.setMaxResults(topX);
		//query.setParameter(3, topX);
		return query.getResultList();
		
	}
	
	@Override
	public List<SaraEntityDB> findByFundAndDomicilationAndRiskCategory(String aFundId, String aCountryId, String rad){
		//select e.*, r.relation_id from sara_legal_fund f, sara_entity e, sara_relation r where f.id = ? and r.lf_id = f.id and r.bp_id = e.id and e.residence_code = ? and r.rad = ?
		TypedQuery<SaraEntityDB> query = em.createQuery("select s from SaraEntityDB s where s.saraFundId = ?1 and s.residenceCode= ?2 and s.rad = ?3",SaraEntityDB.class);
		query.setParameter(1, aFundId);
		query.setParameter(2, aCountryId);
		query.setParameter(3, rad);
		return query.getResultList();
	}
	
	
}