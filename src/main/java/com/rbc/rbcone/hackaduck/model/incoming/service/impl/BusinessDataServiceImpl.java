package com.rbc.rbcone.hackaduck.model.incoming.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rbc.rbcone.hackaduck.model.incoming.RegionRiskDB;
import com.rbc.rbcone.hackaduck.model.incoming.repository.RegionRiskRepository;
import com.rbc.rbcone.hackaduck.model.incoming.repository.SaraRelationRepository;
import com.rbc.rbcone.hackaduck.model.incoming.service.BusinessDataService;

@Service
public class BusinessDataServiceImpl implements BusinessDataService {

	@Autowired
	RegionRiskRepository regionRiskRepository;
	
	@Autowired
	SaraRelationRepository saraRelationRepo;
		
	public void feedBusinessDataForRegions()
	{

		List<RegionRiskDB> rsltList = new ArrayList<RegionRiskDB>();
		
		Object[] rsRows = saraRelationRepo.findAllRegionLevelRelations();
		
		System.out.println("Length: "+rsRows.length);
		
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
}