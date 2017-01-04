package com.rbc.rbcone.hackaduck.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rbc.rbcone.hackaduck.model.EntityRisk;
import com.rbc.rbcone.hackaduck.model.LegalFund;
import com.rbc.rbcone.hackaduck.model.Risk;

@RestController
@RequestMapping("/api")
public class RiskProfileResource {

	@RequestMapping(value="/funds", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
	public List<LegalFund> getFundList() {
		// Mocked data
		ArrayList<LegalFund> rslt = new ArrayList<LegalFund>();
		rslt.add(new LegalFund("1", "LFUND1"));
		rslt.add(new LegalFund("2", "LFUND2"));
		rslt.add(new LegalFund("3", "LFUND3"));
		rslt.add(new LegalFund("4", "LFUND4"));
		rslt.add(new LegalFund("5", "LFUND5"));
		return rslt;
	}
	
	@RequestMapping(value="/funds/{fundId}/regions", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
	public List<EntityRisk> getRegionRiskList(@PathVariable("fundId") String aFundId) {
		ArrayList<EntityRisk> rslt = new ArrayList<EntityRisk>();
		rslt.add(createEntityRisk("AF"));
		rslt.add(createEntityRisk("NA"));
		rslt.add(createEntityRisk("AS"));
		rslt.add(createEntityRisk("EU"));
		return rslt;
	}
	
	@RequestMapping(value="/funds/{fundId}/regions/{regionId}", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
	public EntityRisk getRegionRisk(@PathVariable("fundId") String aFundId, @PathVariable("regionId") String aRegionId) {
		// Mocked data
		return createEntityRisk("EU");
	}
	
	@RequestMapping(value="/funds/{fundId}/regions/{regionId}/countries", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
	public List<EntityRisk> getCountriesRiskList(@PathVariable("fundId") String aFundId) {
		// Mocked data
		ArrayList<EntityRisk> rslt = new ArrayList<EntityRisk>();
		rslt.add(createEntityRisk("GMY"));
		rslt.add(createEntityRisk("FRA"));
		rslt.add(createEntityRisk("ESP"));
		rslt.add(createEntityRisk("IRL"));
		return rslt;
	}
	
	@RequestMapping(value="/funds/{fundId}/countries/{countryId}", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
	public EntityRisk getCountryRisk(@PathVariable("fundId") String aFundId, @PathVariable("countryId") String aCountryId) {
		// Mocked data
		return createEntityRisk("GMY");
	}
	
	
	private EntityRisk createEntityRisk(String anEntityId) {
		EntityRisk rslt = new EntityRisk();
		rslt.setFund(new LegalFund("1", "LFUND1"));
		rslt.setEntityId(anEntityId);
		rslt.setLow(new Risk(25.0, 345, 12));
		rslt.setMedium(new Risk(50.0, 5677, 82));
		rslt.setHigh(new Risk(25.0, 35, 6));
		return rslt;
	}
}
