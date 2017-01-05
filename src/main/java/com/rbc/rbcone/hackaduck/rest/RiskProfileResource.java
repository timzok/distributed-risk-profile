package com.rbc.rbcone.hackaduck.rest;

import com.rbc.rbcone.hackaduck.model.LegalFund;
import com.rbc.rbcone.hackaduck.model.RegionRisk;
import com.rbc.rbcone.hackaduck.model.RegionsRisk;
import com.rbc.rbcone.hackaduck.model.Risk;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RiskProfileResource {

    private ArrayList<LegalFund> funds = new ArrayList<LegalFund>();
	
    public RiskProfileResource() {
        funds.add(createLegalFund("Fund1", "Fund Name 1"));
        funds.add(createLegalFund("Fund2", "Fund Name 2"));
    }
	
    @RequestMapping(value = "/funds", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public List<LegalFund> getFundList() {
        // Mocked data
        return funds;
    }

    @RequestMapping(value = "/funds/{fundId}/regions", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public RegionsRisk getRegionRiskList(@PathVariable("fundId") String aFundId) {
    	LegalFund targetFund = null;
    	for (LegalFund fund : funds) {
    		if (fund.getId().equals(aFundId)) {
    			targetFund = fund;
    		}
    	}
    	if (targetFund==null) {
    		targetFund = funds.get(0);
    	}
    	
    	RegionsRisk rslt = new RegionsRisk();
    	rslt.setFundId(aFundId);
    	rslt.setFundName(targetFund.getName());
    	
    	ArrayList<RegionRisk> regions = new ArrayList<RegionRisk>();
    	regions.add(createRegionRisk("EU", new Risk(2000, 2000, 74, 74), new Risk(500, 500, 18.5, 18.5), new Risk(200, 200, 7.5, 7.5)));
    	regions.add(createRegionRisk("NA", new Risk(25, 25, 17.7, 17.7), new Risk(32, 32, 22.7, 22.7), new Risk(84, 84, 59.5, 59.5)));
    	regions.add(createRegionRisk("OC", new Risk(564, 564, 86, 86), new Risk(12, 12, 1.8, 1.8), new Risk(80, 80, 12.2, 12.2)));
    	regions.add(createRegionRisk("AS", new Risk(159, 159, 51.7, 51.7), new Risk(147, 147, 47.7, 47.7), new Risk(2, 2, 0.6, 0.6)));
    	rslt.setRegions(regions);
        return rslt;
    }

    @RequestMapping(value = "/funds/{fundId}/regions/{regionId}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public RegionRisk getRegionRisk(@PathVariable("fundId") String aFundId, @PathVariable("regionId") String aRegionId) {
        // Mocked data
        return createRegionRisk(aRegionId, new Risk(2000, 2000, 74, 74), new Risk(500, 500, 18.5, 18.5), new Risk(200, 200, 7.5, 7.5));
    }

    @RequestMapping(value = "/funds/{fundId}/regions/{regionId}/countries", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public List<String> getCountriesRiskList(@PathVariable("fundId") String aFundId) {
        // Mocked data
        return null;
    }

    @RequestMapping(value = "/funds/{fundId}/countries/{countryId}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public String getCountryRisk(@PathVariable("fundId") String aFundId, @PathVariable("countryId") String aCountryId) {
        // Mocked data
        return null;
    }


    private RegionRisk createRegionRisk(String aRegionId, Risk aLowRisk, Risk aMediumRisk, Risk aHighRisk) {
    	RegionRisk rslt = new RegionRisk();
    	rslt.setRegionCode(aRegionId);
    	rslt.setLow(aLowRisk);
    	rslt.setMedium(aMediumRisk);
    	rslt.setHigh(aHighRisk);
    	return rslt;
    }
    	
    private LegalFund createLegalFund(String anId, String aName) {
    	LegalFund rslt = new LegalFund(anId, aName);
    	return rslt;
    }
}
