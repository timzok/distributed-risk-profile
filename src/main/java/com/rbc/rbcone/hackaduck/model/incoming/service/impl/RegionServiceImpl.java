package com.rbc.rbcone.hackaduck.model.incoming.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rbc.rbcone.hackaduck.model.incoming.RegionRiskDB;
import com.rbc.rbcone.hackaduck.model.incoming.repository.RegionRiskRepository;
import com.rbc.rbcone.hackaduck.model.incoming.repository.SaraRelationRepository;
import com.rbc.rbcone.hackaduck.model.incoming.service.RegionService;

@Service
public class RegionServiceImpl implements RegionService {

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
			System.out.println("COUNT ENTITY:"+((BigInteger)rsRow[5]).intValue());
			System.out.println(rslt.getId()+"---"+rslt.getFundId()+"---"+rslt.getFundName()+"-----"+rslt.getRegionId()+"------"+rslt.getRad()+"---------"+rslt.getSumAssetValue()+"--------"+rslt.getCountEntity());
			rsltList.add(rslt);	
		}
		
		System.out.println("FINAL SIZE:" + rsltList.size());
		regionRiskRepository.save(rsltList);	
	}
}