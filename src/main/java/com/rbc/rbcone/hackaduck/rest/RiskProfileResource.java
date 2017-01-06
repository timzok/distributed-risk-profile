package com.rbc.rbcone.hackaduck.rest;

import com.rbc.rbcone.hackaduck.exception.UnknownObjectException;
import com.rbc.rbcone.hackaduck.model.AbstractEntityRisk;
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
import com.rbc.rbcone.hackaduck.model.incoming.SaraPeps;
import com.rbc.rbcone.hackaduck.model.incoming.repository.SaraEntityRepository;
import com.rbc.rbcone.hackaduck.model.incoming.repository.SaraLegalFundRepository;
import com.rbc.rbcone.hackaduck.model.incoming.repository.SaraPepsRepository;
import com.rbc.rbcone.hackaduck.model.incoming.repository.SaraRelationRepository;
import com.rbc.rbcone.hackaduck.model.Peps;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class RiskProfileResource {
	// Mocked funds
    private ArrayList<LegalFund> funds = new ArrayList<LegalFund>();
	
    private static final boolean IS_MOCKED = false;
    private HashMap<String, Double[]> fundIdToGlobalAssetPerRiskCategorykMap = new HashMap<String, Double[]>();
    
    
    @Autowired
    private SaraLegalFundRepository saraLegalFundRepo;
    @Autowired
    private SaraRelationRepository saraRelationRepo;
    @Autowired
    private SaraPepsRepository saraPepsRepo;
    @Autowired
    private SaraEntityRepository saraEntityRepo;
    
    public RiskProfileResource() {
    	if (IS_MOCKED) {
        	// Mocked data initialization
            funds.add(createLegalFund("Fund1", "Fund Name 1"));
            funds.add(createLegalFund("Fund2", "Fund Name 2"));
    	}
    }
	
    @RequestMapping(value = "/funds", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public List<LegalFund> getFundList() {
    	if (IS_MOCKED) {
    		return funds;
    	} else {
    	   	ArrayList<LegalFund> localFunds = new ArrayList<LegalFund>();
    	   	for(SaraLegalFund cse: saraLegalFundRepo.findAll()) {
    	    		localFunds.add(new LegalFund(cse.getId(),cse.getName()));
    		}	
    	   	return localFunds;
    	}
    }

    
    @RequestMapping(value = "/funds/{fundId}/regions", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public RegionsRisk getRegionRiskList(@PathVariable("fundId") String aFundId) {
    	if (IS_MOCKED) {
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
    	} else {    	
	    	RegionsRisk rslt = new RegionsRisk();
			
			// select f.id as fundId, f.name, g.name as regionId, r.rad, sum(r.asset_Value), count(distinct e.id) 
			// from country c, region g, sara_legal_fund f, sara_entity e, sara_relation r 
			// where f.id = ? and r.lf_id = f.id and r.bp_id = e.id 
			// and e.residence_code = c.type and c.region_id = g.id group by g.id, r.rad
			
			// LEM / MATETAM RATET / NA  / H / 0,0 / 1
			
			Object[] rsRows = saraRelationRepo.findRegionLevelRelations(aFundId);
			
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
			
			fundIdToGlobalAssetPerRiskCategorykMap.put(aFundId, new Double[]{globalAssetValueLowCategory, globalAssetValueMediumCategory, globalAssetValueHighCategory});
			
			for (RegionRisk region : rslt.getRegions()) {
				region.getLow().setGlobalAssetValue(globalAssetValueLowCategory);
				region.getMedium().setGlobalAssetValue(globalAssetValueMediumCategory);
				region.getHigh().setGlobalAssetValue(globalAssetValueHighCategory);
			}
			return rslt;
    	}		
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

    @RequestMapping(value = "/funds/{fundId}/regions/{regionId}/countries", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public CountriesRisk getCountriesRiskList(@PathVariable("fundId") String aFundId, @PathVariable("regionId") String aRegionId) {
    	if (IS_MOCKED) {
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
    	} else {
			CountriesRisk rslt = new CountriesRisk();
			rslt.setRegionCode(aRegionId);
		
			
			// select f.id as fundId, f.name, c.type as countryCode, c.label as countryName, r.rad, sum(r.asset_Value), count(distinct e.id) 
			// from country c, region g, sara_legal_fund f, sara_entity e, sara_relation r 
			// where f.id = ? and g.name = ? and c.region_id = g.id and r.lf_id = f.id and r.bp_id = e.id and e.residence_code = c.type group by c.type, r.rad")
			
			Object[] rsRows = saraRelationRepo.findCountryLevelRelations(aFundId, aRegionId);
			
			for (int i=0; i<rsRows.length;i++) {
				Object[] rsRow = (Object[])rsRows[i];
				
				if (i==0) {
					rslt.setFundId((String)rsRow[0]);
					rslt.setFundName((String)rsRow[1]);
				}
				
				String countryCode = (String)rsRow[2];
				CountryRisk countryRisk = rslt.getCountryRisk(countryCode);
				if (countryRisk==null) {
					countryRisk = new CountryRisk();
					countryRisk.setCountryCode(countryCode);
					countryRisk.setCountryName((String)rsRow[3]);
					rslt.getCountries().add(countryRisk);
				}
				
				String riskCategory = (String)rsRow[4];
				Risk risk = new Risk(((BigInteger)rsRow[6]).intValue(), (double)rsRow[5], 0d);	
				countryRisk.setRisk(risk, riskCategory);
			}
		
			Double[] globalAssetPerRiskCategory = fundIdToGlobalAssetPerRiskCategorykMap.get(aFundId);
			
			for (CountryRisk country : rslt.getCountries()) {
				country.fillMissingRisks();
				
				country.getLow().setGlobalAssetValue(globalAssetPerRiskCategory[0]);
				country.getMedium().setGlobalAssetValue(globalAssetPerRiskCategory[1]);
				country.getHigh().setGlobalAssetValue(globalAssetPerRiskCategory[2]);
			}
			return rslt;
    	}
    }

    @RequestMapping(value = "/funds/{fundId}/regions/{regionId}/topcountries/{topCount}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public CountriesRisk getTopCountriesRiskList(@PathVariable("fundId") String aFundId, @PathVariable("regionId") String aRegionId, @PathVariable("topCount") int aTopCount) {
    	if (IS_MOCKED) {
            // Mocked data
        	CountriesRisk rslt = getCountriesRiskList(aFundId, aRegionId);
        	List<CountryRisk> countryRisk = rslt.getCountries();
        	int endIndex = Math.min(countryRisk.size(), aTopCount);
        	rslt.setCountries(countryRisk.subList(0, endIndex));
            return rslt;
    	} else {
	    	CountriesRisk rslt = new CountriesRisk();
	    	rslt.setRegionCode(aRegionId);
	
	    	// select top 10 * 
	    	// from (
	    	// 	select f.id as fundId, f.name as fundName, c.type as countryCode, c.label as countryName, sum(r.asset_Value) as assetValueSum, count(distinct e.id) as investorCount 
	    	//  from country c, region g, sara_legal_fund f, sara_entity e, sara_relation r 
	    	//  where f.id = ? and g.name = ? and c.region_id = g.id and r.lf_id = f.id and r.bp_id = e.id and r.rad = 'H' and e.residence_code = c.type group by c.type) order by assetValueSum desc")
			
			Object[] rsRows = saraRelationRepo.findTopCountryLevelRelations(aTopCount, aFundId, aRegionId);
			
			for (int i=0; i<rsRows.length;i++) {
				Object[] rsRow = (Object[])rsRows[i];
				
				if (i==0) {
					rslt.setFundId((String)rsRow[0]);
					rslt.setFundName((String)rsRow[1]);
				}
				
				String countryCode = (String)rsRow[2];
				CountryRisk countryRisk = rslt.getCountryRisk(countryCode);
				if (countryRisk==null) {
					countryRisk = new CountryRisk();
					countryRisk.setCountryCode(countryCode);
					countryRisk.setCountryName((String)rsRow[3]);
					rslt.getCountries().add(countryRisk);
				}
				
				Risk risk = new Risk(((BigInteger)rsRow[5]).intValue(), (double)rsRow[4], 0d);	
				countryRisk.setRisk(risk, AbstractEntityRisk.HIGH_RISK_CATEGORY);
			}
	
			Double[] globalAssetPerRiskCategory = fundIdToGlobalAssetPerRiskCategorykMap.get(aFundId);
			
			for (CountryRisk country : rslt.getCountries()) {
				country.getHigh().setGlobalAssetValue(globalAssetPerRiskCategory[2]);
			}
			return rslt;
    	} 
    }

    @RequestMapping(value = "/funds/{fundId}/countries/{countryId}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public CountryDetailRisk getCountryRisk(@PathVariable("fundId") String aFundId, @PathVariable("countryId") String aCountryId) {
    	if (IS_MOCKED) {
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
    	} else {
    		CountryDetailRisk rslt = new CountryDetailRisk();

        	// select f.id as fundId, f.name, g.name as regionCode, c.type as countryCode, c.label as countryName, r.rad, sum(r.asset_Value), count(distinct e.id) 
        	// from country c, region g, sara_legal_fund f, sara_entity e, sara_relation r 
        	// where f.id = ? and c.type = ? and c.region_id = g.id and r.lf_id = f.id and r.bp_id = e.id and e.residence_code = c.type group by c.type, r.rad

    		Object[] rsRows = saraRelationRepo.findCountryRelations(aFundId, aCountryId);
    		
    		for (int i=0; i<rsRows.length;i++) {
    			Object[] rsRow = (Object[])rsRows[i];
    			
    			if (i==0) {
    				rslt.setFundId((String)rsRow[0]);
    				rslt.setFundName((String)rsRow[1]);
    	        	rslt.setCountryCode((String)rsRow[3]);
    	        	rslt.setCountryName((String)rsRow[4]);
    			}
    			
    			String riskCategory = (String)rsRow[5];
    			Risk risk = new Risk(((BigInteger)rsRow[7]).intValue(), (double)rsRow[6], 0d);	
    			rslt.setRisk(risk, riskCategory);
    		}

    		Double[] globalAssetPerRiskCategory = fundIdToGlobalAssetPerRiskCategorykMap.get(aFundId);
    		
    		rslt.fillMissingRisks();
    		rslt.getLow().setGlobalAssetValue(globalAssetPerRiskCategory[0]);
    		rslt.getMedium().setGlobalAssetValue(globalAssetPerRiskCategory[1]);
    		rslt.getHigh().setGlobalAssetValue(globalAssetPerRiskCategory[2]);
    		
    		return rslt;
    	}
    }

    @RequestMapping(value = "/funds/{fundId}/countries/{countryId}/legalEntities/rads/{rad}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public CountryLegalEntityRisk getLegalEntityPeps(@PathVariable("fundId") String aFundId, @PathVariable("countryId") String aCountryId, @PathVariable("rad") String aRiskCategory) throws UnknownObjectException {
    	return getLegalEntityPepsHelper(aFundId, aCountryId, aRiskCategory);
    }
    

    @RequestMapping(value = "/funds/{fundId}/countries/{countryId}/legalEntitiesExport/rads/{rad}", method = RequestMethod.GET, produces = {"text/csv; charset=UTF-8"})
    public void getLegalEntityPepsCSV(@PathVariable("fundId") String aFundId, @PathVariable("countryId") String aCountryId, @PathVariable("rad") String aRiskCategory, HttpServletResponse aResponse) throws IOException, UnknownObjectException {
    	aResponse.setContentType("text/plain; charset=utf-8");
    	String fileName = generateFileName(aFundId, aCountryId, aRiskCategory);
    	aResponse.addHeader("Content-Disposition", "attachment;filename=" + fileName);
    	aResponse.addHeader("Expire", "0");
    	aResponse.addHeader("Pragma", "no-cache");
    	aResponse.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");

    	OutputStreamWriter out = new OutputStreamWriter(aResponse.getOutputStream());
    	CSVPrinter csvFilePrinter = new CSVPrinter(out, CSVFormat.DEFAULT.withDelimiter(';').withRecordSeparator('\n').withEscape('|').withQuoteMode(QuoteMode.NONE));
    	
    	CountryLegalEntityRisk rslt = getLegalEntityPepsHelper(aFundId, aCountryId, aRiskCategory);
    	
    	// CSV header
    	csvFilePrinter.printRecord("Legal Entity Name", "Legal Entity Type", "Legal Entity Nature", "Peps First Name", "Peps Last Name", "Peps Role", "Peps Country Code");
    	
    	// CSV body
    	for (LegalEntity legalEntity : rslt.getLegalEntities()) {
    		for (Peps peps : legalEntity.getPeps()) {
    			csvFilePrinter.printRecord(legalEntity.getName(), legalEntity.getType(), legalEntity.getNature(), peps.getFirstName(), peps.getLastName(), peps.getRole(), peps.getCountry());
    		}
    	}
    	
    	out.flush();
    	csvFilePrinter.close();
    }
    
    private CountryLegalEntityRisk getLegalEntityPepsHelper(String aFundId, String aCountryId, String aRiskCategory) throws UnknownObjectException {
    	if (IS_MOCKED) {
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
    	} else {
	    	CountryLegalEntityRisk rslt = new CountryLegalEntityRisk();
	    	rslt.setCountryCode(aCountryId);
	    	rslt.setRad(aRiskCategory);
	    	
	    	SaraLegalFund legalFund = saraLegalFundRepo.findOne(aFundId);
	    	if (legalFund==null) {
	    		throw new UnknownObjectException("No fund found identified by fund id '" + aFundId + "'");
	    	}
	    	rslt.setFundId(legalFund.getId());
	    	rslt.setFundName(legalFund.getName());
	    	
    		List<SaraPeps> saraPepsList = saraPepsRepo.findPepsByFundAndCountryAndRiskCategory(aFundId, aCountryId, aRiskCategory);
    		List<Object[]> saraEntityList = saraEntityRepo.findByFundAndDomicilationAndRiskCategory(aFundId, aCountryId, aRiskCategory);

    		HashMap<String, LegalEntity> legalEntityIdToLegalEntityMap = new HashMap<String, LegalEntity>();
    		String legalEntityId = null; LegalEntity legalEntity = null; String relationId = null;
    		
    		for (Object[] saraEntityRsRow : saraEntityList) {
    			legalEntityId = (String)saraEntityRsRow[0];
    			legalEntity = legalEntityIdToLegalEntityMap.get(legalEntityId);
    			
    			if (legalEntity==null) {
    				legalEntity = new LegalEntity((String)saraEntityRsRow[1], (String)saraEntityRsRow[4], (String)saraEntityRsRow[2], new ArrayList<Peps>());
    				rslt.getLegalEntities().add(legalEntity);
    				legalEntityIdToLegalEntityMap.put(legalEntityId, legalEntity);
    			}
    			
    			relationId = (String)saraEntityRsRow[5];
    			for (SaraPeps saraPeps : saraPepsList) {
    				if (relationId.equals(saraPeps.getRelationId())) {
    					legalEntity.getPeps().add(new Peps(saraPeps.getFirstName(), saraPeps.getLastName(), saraPeps.getRole(), saraPeps.getCountry()));
    				}
    			}
    		}
    		
    		return rslt;
    	}
    }
    
    
    private String generateFileName(String aFundId, String aCountryId, String aRiskCategory) {
    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
    	String timestampStr = format.format(new Date());
    	return "peps_" + aFundId + "_" + aCountryId + "_" + aRiskCategory + "_" + timestampStr + ".csv";
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
