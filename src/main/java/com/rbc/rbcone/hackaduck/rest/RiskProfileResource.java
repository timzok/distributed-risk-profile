package com.rbc.rbcone.hackaduck.rest;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rbc.rbcone.hackaduck.model.EntityRisk;
import com.rbc.rbcone.hackaduck.model.LegalFund;

@RestController
@RequestMapping("/api")
public class RiskProfileResource {

	@RequestMapping(value="/funds", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
	public List<LegalFund> getFundList() {
		return null;
	}
	
	@RequestMapping(value="/funds/{fundId}/regions", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
	public List<EntityRisk> getRegionRiskList(@PathVariable("fundId") String aFundId) {
		return null;
	}
	
	@RequestMapping(value="/funds/{fundId}/regions/{regionId}", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
	public EntityRisk getRegionRisk(@PathVariable("fundId") String aFundId, @PathVariable("regionId") String aRegionId) {
		return null;
	}
	
	@RequestMapping(value="/funds/{fundId}/regions/{regionId}/countries", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
	public List<EntityRisk> getCountriesRiskList(@PathVariable("fundId") String aFundId) {
		return null;
	}
	
	@RequestMapping(value="/funds/{fundId}/countries/{countryId}", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
	public List<EntityRisk> getCountryRisk(@PathVariable("fundId") String aFundId, @PathVariable("countryId") String aCountryId) {
		return null;
	}
	
}
