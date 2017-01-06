package com.rbc.rbcone.hackaduck.rest;

import com.rbc.rbcone.hackaduck.model.CountriesRisk;
import com.rbc.rbcone.hackaduck.model.CountryDetailRisk;
import com.rbc.rbcone.hackaduck.model.CountryLegalEntityRisk;
import com.rbc.rbcone.hackaduck.model.CountryRisk;
import com.rbc.rbcone.hackaduck.model.LegalEntity;
import com.rbc.rbcone.hackaduck.model.LegalFund;
import com.rbc.rbcone.hackaduck.model.RegionRisk;
import com.rbc.rbcone.hackaduck.model.RegionsRisk;
import com.rbc.rbcone.hackaduck.model.Risk;
import com.rbc.rbcone.hackaduck.model.incoming.SaraLegalFund;
import com.rbc.rbcone.hackaduck.model.incoming.repository.SaraLegalFundRepository;
import com.rbc.rbcone.hackaduck.model.incoming.repository.SaraRelationRepository;
import com.rbc.rbcone.hackaduck.model.Peps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class RiskProfileResource {

    private ArrayList<LegalFund> funds = new ArrayList<LegalFund>();
	
    @Autowired
    private SaraLegalFundRepository saraLegalFundRepo;
    @Autowired
    private SaraRelationRepository saraRelationRepo;
    
    public RiskProfileResource() {
        funds.add(createLegalFund("Fund1", "Fund Name 1"));
        funds.add(createLegalFund("Fund2", "Fund Name 2"));
    }
	
    @RequestMapping(value = "/funds", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public List<LegalFund> getFundList() {
    	
   	ArrayList<LegalFund> localFunds = new ArrayList<LegalFund>();

   	for(SaraLegalFund cse: saraLegalFundRepo.findAll())
		{
    		localFunds.add(new LegalFund(cse.getId(),cse.getName()));
		}
    	
   	return localFunds;
    }

    
    @RequestMapping(value = "/funds/{fundId}/regions", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public RegionsRisk getRegionRiskList(@PathVariable("fundId") String aFundId) {
    	
    	/*RegionsRisk rslt = new RegionsRisk();
    	rslt.setFundId(aFundId);
    	rslt.setFundName("toto");

		System.out.println("Before exec");
		
		// select f.id as fundId, f.name, g.name as regionId, r.rad, sum(r.asset_Value), count(distinct e.id) 
		// from country c, region g, sara_legal_fund f, sara_entity e, sara_relation r 
		// where f.id = ? and r.lf_id = f.id and r.bp_id = e.id 
		// and e.residence_code = c.type and c.region_id = g.id group by g.id, r.rad
		
		// LEM / MATETAM RATET / NA  / H / 0,0 / 1
		
		Object[] rsRows = saraRelationRepo.findRegionLevelRelations(aFundId);
		System.out.println("After exec " + rsRows.length);
		
		for (int i=0; i<rsRows.length;i++) {
			Object[] rsRow = (Object[])rsRows[i];
			
			if (i==0) {
				rslt.setFundId((String)rsRow[0]); //LEM
				rslt.setFundName((String)rsRow[1]); //MATETAM RATET
			}
			
			String regionCode = (String)rsRow[2]; //NA
			RegionRisk regionRisk = rslt.getRegionRisk(regionCode);
			if (regionRisk==null) {
				regionRisk = new RegionRisk();
				regionRisk.setRegionCode(regionCode);
				rslt.getRegions().add(regionRisk);
			}
			
			String riskCategory = (String)rsRow[3]; //H
			Risk risk = new Risk(((BigInteger)rsRow[5]).intValue(), (double)rsRow[4], 0d);		 //1 - 0,0	
			regionRisk.setRisk(risk, riskCategory);
		}

		double globalAssetValueLowCategory = 0.0d;
		double globalAssetValueMediumCategory = 0.0d;
		double globalAssetValueHighCategory = 0.0d;
		
		for (RegionRisk region : rslt.getRegions()) {
			region.fillMissingRisks();
			
			globalAssetValueLowCategory += region.getLow().getAssetValue();
			globalAssetValueMediumCategory += region.getMedium().getAssetValue();
			globalAssetValueHighCategory += region.getHigh().getAssetValue();
		}
		
		for (RegionRisk region : rslt.getRegions()) {
			region.getLow().setGlobalAssetValue(globalAssetValueLowCategory);
			region.getMedium().setGlobalAssetValue(globalAssetValueMediumCategory);
			region.getHigh().setGlobalAssetValue(globalAssetValueHighCategory);
		}		
		*/

    	LegalFund targetFund = findLegalFund(aFundId);
    	RegionsRisk rslt = new RegionsRisk();
    	rslt.setFundId(aFundId);
    	rslt.setFundName(targetFund.getName());
    	
    	ArrayList<RegionRisk> regions = new ArrayList<RegionRisk>();
    	ArrayList<RegionRisk> finalRegions = new ArrayList<RegionRisk>();
    	
    	regions.add(createRegionRisk("AS", new Risk((int)(2000 * Math.random()), 74, 74), new Risk(500,  18.5, 18.5), new Risk(200,  7.5, 7.5)));
    	regions.add(createRegionRisk("OC", new Risk((int)(25   * Math.random()), 17.7, 17.7), new Risk(32,  22.7, 22.7), new Risk(84,  59.5, 59.5)));
    	regions.add(createRegionRisk("EU", new Risk((int)(564  * Math.random()), 86, 86), new Risk(12,  1.8, 1.8), new Risk(80,  12.2, 12.2)));
    	regions.add(createRegionRisk("SA", new Risk((int)(159  * Math.random()), 51.7, 51.7), new Risk(147,  47.7, 47.7), new Risk(2,  0.6, 0.6)));
    	regions.add(createRegionRisk("AF", new Risk((int)(159  * Math.random()), 51.7, 51.7), new Risk(147,  47.7, 47.7), new Risk(2,  0.6, 0.6)));
    	regions.add(createRegionRisk("NA", new Risk((int)(159  * Math.random()), 51.7, 51.7), new Risk(147,  47.7, 47.7), new Risk(2,  0.6, 0.6)));


    	
    	
    	int randomI = (int)(Math.random() * (6 - 3)) + 3;
    	List<Integer> uniqueRandomList = new ArrayList<Integer>();
    	
    	for (int nbResult=0; nbResult<randomI;nbResult++)
    	{
    		int secondRandom = calculateUniqueRandom(uniqueRandomList);
    		uniqueRandomList.add(secondRandom);
    		
    		finalRegions.add(regions.get(secondRandom));
    		
    	}
    	rslt.setRegions(finalRegions);
    	/*if ("Fund1".equals(aFundId)) {
        	    	} else {
     		//AF/AS/EU/NA/OC/SA
           	regions.add(createRegionRisk("AS", new Risk((int)(2000 * Math.random()), 74, 74), new Risk(500,  18.5, 18.5), new Risk(200,  7.5, 7.5)));
        	regions.add(createRegionRisk("OC", new Risk((int)(25   * Math.random()), 17.7, 17.7), new Risk(32,  22.7, 22.7), new Risk(84,  59.5, 59.5)));
        	regions.add(createRegionRisk("EU", new Risk((int)(564  * Math.random()), 86, 86), new Risk(12,  1.8, 1.8), new Risk(80,  12.2, 12.2)));
        	regions.add(createRegionRisk("SA", new Risk((int)(159  * Math.random()), 51.7, 51.7), new Risk(147,  47.7, 47.7), new Risk(2,  0.6, 0.6)));
    	}*/
    	//rslt.setRegions(regions);
  	
        return rslt;
    }
    
    private int calculateUniqueRandom(List<Integer> existingList)
    {
    	int randomI = (int)(Math.random() * (5)) + 1;
    	if(existingList == null || existingList.size()<1) return randomI;
    	
    	for (int existing: existingList)
    	{
    		if (existing == randomI) return calculateUniqueRandom(existingList);
    	}
    	return randomI;
    }

    
    @RequestMapping(value = "/funds/{fundId}/regions/{regionId}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public RegionRisk getRegionRisk(@PathVariable("fundId") String aFundId, @PathVariable("regionId") String aRegionId) {
        // Mocked data
        return createRegionRisk(aRegionId, new Risk(2000, 74, 74), new Risk(500, 18.5, 18.5), new Risk(200, 7.5, 7.5));
    }

    @RequestMapping(value = "/funds/{fundId}/regions/{regionId}/countries", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public CountriesRisk getCountriesRiskList(@PathVariable("fundId") String aFundId, @PathVariable("regionId") String aRegionId) {
        // Mocked data
    	LegalFund targetFund = findLegalFund(aFundId);
    	CountriesRisk rslt = new CountriesRisk();
    	rslt.setFundId(aFundId);
    	rslt.setFundName(targetFund.getName());
    	rslt.setRegionCode(aRegionId);
    	
    	ArrayList<CountryRisk> countries = new ArrayList<CountryRisk>();
    	countries.add(createCountryRisk("LU", "Luxembourg", new Risk(2000, 74, 74), new Risk(500,  18.5, 18.5), new Risk(200,  7.5, 7.5)));
    	countries.add(createCountryRisk("FR", "France", new Risk(25, 17.7, 17.7), new Risk(32,  22.7, 22.7), new Risk(84,  59.5, 59.5)));
    	countries.add(createCountryRisk("BE", "Belgium", new Risk(564, 86, 86), new Risk(12,  1.8, 1.8), new Risk(80,  12.2, 12.2)));
    	countries.add(createCountryRisk("LT", "Lituania", new Risk(159, 51.7, 51.7), new Risk(147,  47.7, 47.7), new Risk(2,  0.6, 0.6)));
    	countries.add(createCountryRisk("UA", "Ukrania", new Risk(159, 51.7, 51.7), new Risk(147,  47.7, 47.7), new Risk(2,  0.6, 0.6)));
    	countries.add(createCountryRisk("DE", "Germany", new Risk(159, 51.7, 51.7), new Risk(147,  47.7, 47.7), new Risk(2,  0.6, 0.6)));
    	countries.add(createCountryRisk("HR", "Croatia", new Risk(159, 51.7, 51.7), new Risk(147,  47.7, 47.7), new Risk(2,  0.6, 0.6)));
    	countries.add(createCountryRisk("PL", "Poland", new Risk(159, 51.7, 51.7), new Risk(147,  47.7, 47.7), new Risk(2,  0.6, 0.6)));
    	countries.add(createCountryRisk("SE", "Sweden", new Risk(159, 51.7, 51.7), new Risk(147,  47.7, 47.7), new Risk(2,  0.6, 0.6)));
    	countries.add(createCountryRisk("SI", "Slovenia", new Risk(159, 51.7, 51.7), new Risk(147,  47.7, 47.7), new Risk(2,  0.6, 0.6)));
    	countries.add(createCountryRisk("RU", "Russia", new Risk(159, 51.7, 51.7), new Risk(147, 47.7, 47.7), new Risk(2,  0.6, 0.6)));
    	rslt.setCountries(countries);
        return rslt;
    }

    @RequestMapping(value = "/funds/{fundId}/regions/{regionId}/topcountries/{topCount}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public CountriesRisk getTopCountriesRiskList(@PathVariable("fundId") String aFundId, @PathVariable("regionId") String aRegionId, @PathVariable("topCount") int aTopCount) {
        // Mocked data
    	CountriesRisk rslt = getCountriesRiskList(aFundId, aRegionId);
    	List<CountryRisk> countryRisk = rslt.getCountries();
    	int endIndex = Math.min(countryRisk.size(), aTopCount);
    	rslt.setCountries(countryRisk.subList(0, endIndex));
        return rslt;
    }

    @RequestMapping(value = "/funds/{fundId}/countries/{countryId}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public CountryDetailRisk getCountryRisk(@PathVariable("fundId") String aFundId, @PathVariable("countryId") String aCountryId) {
        // Mocked data
    	LegalFund targetFund = findLegalFund(aFundId);
    	CountryDetailRisk rslt = new CountryDetailRisk();
    	rslt.setFundId(targetFund.getId());
    	rslt.setFundName(targetFund.getName());
    	rslt.setCountryCode(aCountryId);
    	rslt.setCountryName("Luxembourg");
    	rslt.setLow(new Risk((int)(2000* Math.random()),  74* Math.random(), 74* Math.random()));
    	rslt.setMedium(new Risk((int)(500* Math.random()),  50* Math.random(), 50* Math.random()));
    	rslt.setHigh(new Risk((int)(200* Math.random()),  100* Math.random(), 100* Math.random()));
        return rslt; 
    }

    @RequestMapping(value = "/funds/{fundId}/countries/{countryId}/legalEntities/rads/{rad}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public CountryLegalEntityRisk getLegalEntityPeps(@PathVariable("fundId") String aFundId, @PathVariable("countryId") String aCountryId, @PathVariable("rad") String aRiskCategory) {
        // Mocked data
    	LegalFund targetFund = findLegalFund(aFundId);
    	CountryLegalEntityRisk rslt = new CountryLegalEntityRisk();
    	rslt.setFundId(targetFund.getId());
    	rslt.setFundName(targetFund.getName());
    	rslt.setCountryCode(aCountryId);
    	rslt.setRad(aRiskCategory);
    	
    	ArrayList<LegalEntity> entities = new ArrayList<LegalEntity>();
    	entities.add(new LegalEntity("LegalEntity1", "Other Financial Institution", "Legal", Arrays.asList(new Peps("Toto", "Van Der Meulen", "Administrator", "LU"), new Peps("Titi","Dooren","ShareHolder","BE"))));
    	entities.add(new LegalEntity("LegalEntity2", "Other Financial Institution", "Legal", Arrays.asList(new Peps("Tutu","Vonckens","ShareHolder","FR"))));
    	
    	rslt.setLegalEntities(entities);
        return rslt;
    }
    

    //**************************************************************************
    //**** Helper methods for mocking ******************************************
    //**************************************************************************
    
    private RegionRisk createRegionRisk(String aRegionId, Risk aLowRisk, Risk aMediumRisk, Risk aHighRisk) {
    	RegionRisk rslt = new RegionRisk();
    	rslt.setRegionCode(aRegionId);
    	rslt.setLow(aLowRisk);
    	rslt.setMedium(aMediumRisk);
    	rslt.setHigh(aHighRisk);
    	return rslt;
    }

    private CountryRisk createCountryRisk(String aCountryId, String aCountryName, Risk aLowRisk, Risk aMediumRisk, Risk aHighRisk) {
    	CountryRisk rslt = new CountryRisk();
    	rslt.setCountryCode(aCountryId);
    	rslt.setCountryName(aCountryName);
    	rslt.setLow(aLowRisk);
    	rslt.setMedium(aMediumRisk);
    	rslt.setHigh(aHighRisk);
    	return rslt;
    }
    	
    private LegalFund createLegalFund(String anId, String aName) {
    	LegalFund rslt = new LegalFund(anId, aName);
    	return rslt;
    }
    
    private LegalFund findLegalFund(String aFundId) {
    	LegalFund targetFund = null;
    	for (LegalFund fund : funds) {
    		if (fund.getId().equals(aFundId)) {
    			targetFund = fund;
    		}
    	}
    	if (targetFund==null) {
    		targetFund = funds.get(0);
    	}
    	return targetFund;
    }
}
