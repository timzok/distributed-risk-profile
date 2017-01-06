package com.rbc.rbcone.hackaduck.model.incoming.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rbc.rbcone.hackaduck.model.CountriesRisk;
import com.rbc.rbcone.hackaduck.model.incoming.CountryRiskDB;
import com.rbc.rbcone.hackaduck.model.incoming.RegionRiskDB;
import com.rbc.rbcone.hackaduck.model.incoming.repository.CountryRiskRepository;
import com.rbc.rbcone.hackaduck.model.incoming.repository.RegionRiskRepository;
import com.rbc.rbcone.hackaduck.model.incoming.repository.SaraRelationRepository;
import com.rbc.rbcone.hackaduck.model.incoming.service.BusinessDataService;

@Service
public class BusinessDataServiceImpl implements BusinessDataService {

	@Autowired
	RegionRiskRepository regionRiskRepository;
	
	@Autowired
	CountryRiskRepository countryRiskRepository;
	
	@Autowired
	SaraRelationRepository saraRelationRepo;
		
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
		
		System.out.println("FINAL SIZE:" + rsltList.size());
		regionRiskRepository.save(rsltList);	
	}
	
	/*select f.id as fundId, f.name, c.type as countryCode, c.label as countryName, r.rad, sum(r.asset_Value), count(distinct e.id) 
	 * from country c, region g, sara_legal_fund f, sara_entity e, sara_relation r 
	 * where c.region_id = g.id and r.lf_id = f.id and r.bp_id = e.id and e.residence_code = c.type 
	 * group by f.id,c.type, r.rad*/
	
	// ADD GROUP NAME
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
			
			System.out.println(rsRow[0]+"-"+rsRow[1]+"-"+rsRow[2]+"-"+rsRow[3]+"-"+rsRow[4]+"-"+rsRow[5]+"-"+rsRow[6]+"-"+rsRow[7]);
			
			rslt.setFundId((String)rsRow[0]);
			rslt.setFundName((String)rsRow[1]);
			rslt.setCountryCode((String)rsRow[2]);
			rslt.setCountryName((String)rsRow[3]);
			rslt.setRad((String)rsRow[4]);
			rslt.setRegionId((int)rsRow[5]);
			rslt.setSumAssetValue((double)rsRow[6]);
			rslt.setCountEntity(((BigInteger)rsRow[7]).intValue());
			rsltList.add(rslt);
		}
		
		System.out.println("FINAL SIZE:" + rsltList.size());
		countryRiskRepository.save(rsltList);
	
	}
}