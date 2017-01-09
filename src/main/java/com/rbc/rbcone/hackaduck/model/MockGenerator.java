package com.rbc.rbcone.hackaduck.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MockGenerator {

	private HashMap<String, String> countryMap = new HashMap<String, String>();
	private int count = 0;
	
	public MockGenerator() {
		countryMap.put("LU", "Luxembourg");
		countryMap.put("FR", "France");
		countryMap.put("BE", "Belgium");
		countryMap.put("LT", "Lithuania");
		countryMap.put("UA", "Ukraine");
		countryMap.put("DE", "Germany");
		countryMap.put("HR", "Croatia");
		countryMap.put("PL", "Poland");
		countryMap.put("SE", "Sweden");
		countryMap.put("SI", "Slovenia");
		countryMap.put("RU", "Russia");
		countryMap.put("US", "United States");
		countryMap.put("CA", "Canada");
		countryMap.put("BJ", "Benin");
		countryMap.put("ZA", "South Africa");
		countryMap.put("BW", "Botswana");
		countryMap.put("DZ", "Algeria");
		countryMap.put("AU", "Australia");
		countryMap.put("JP", "Japan");
	}
	
	public ArrayList<LegalFund> generateLegalFunds() {
		ArrayList<LegalFund> rslt = new ArrayList<LegalFund>(); 
        rslt.add(new LegalFund("Fund1", "Fund Name 1"));
        rslt.add(new LegalFund("Fund2", "Fund Name 2"));
        return rslt;
	}
	
	public List<RegionAndCountriesRisk> generateWorldwideRiskData() {
		ArrayList<RegionAndCountriesRisk> rslt = new ArrayList<RegionAndCountriesRisk>();
		
		if (count==0) {
			rslt.add(generateRegionAndCountriesRiskData("EU", "LU", "FR", "BE", "LT", "UA", "DE", "HR", "PL", "SE", "SI", "RU"));
			rslt.add(generateRegionAndCountriesRiskData("NA", "US", "CA"));
			rslt.add(generateRegionAndCountriesRiskData("AF", "BJ", "ZA", "BW", "DZ"));
			count=1;
		} else {
			rslt.add(generateRegionAndCountriesRiskData("EU", "LU", "FR", "BE", "LT", "UA", "DE", "HR", "PL", "SE", "SI", "RU"));
			rslt.add(generateRegionAndCountriesRiskData("NA", "US", "CA"));
			rslt.add(generateRegionAndCountriesRiskData("OC", "AU"));
			rslt.add(generateRegionAndCountriesRiskData("AS", "JP"));
			count=0;
		}
		
		return rslt;
	}
	
	private RegionAndCountriesRisk generateRegionAndCountriesRiskData(String aRegionCode, String... aCountryCodes) {
		RegionAndCountriesRisk rslt = new RegionAndCountriesRisk(aRegionCode);

		for (String countryCode : aCountryCodes) {
        	rslt.addCountryRisk(createCountryRisk(countryCode, countryMap.get(countryCode), 
    			new Risk((int)(100*Math.random()), 100000000*Math.random(), 0), 
    			new Risk((int)(100*Math.random()), 100000000*Math.random(), 0), 
    			new Risk((int)(100*Math.random()), 100000000*Math.random(), 0)));
		}
		
		return rslt.consolidate();
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
	
}
