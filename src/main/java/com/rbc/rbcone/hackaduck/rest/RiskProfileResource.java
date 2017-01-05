package com.rbc.rbcone.hackaduck.rest;

import com.rbc.rbcone.hackaduck.model.LegalFund;
import com.rbc.rbcone.hackaduck.model.Risk;
import com.rbc.rbcone.hackaduck.model.RiskEntity;
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

    @RequestMapping(value = "/funds", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public List<LegalFund> getFundList() {
        // Mocked data
        ArrayList<LegalFund> rslt = new ArrayList<LegalFund>();
        rslt.add(createLegalFund("1", "Fund1"));
        rslt.add(createLegalFund("2", "Fund2"));
//        rslt.add(createLegalFund("3", "LFUND3"));
//        rslt.add(createLegalFund("4", "LFUND4"));
//        rslt.add(createLegalFund("5", "LFUND5"));
        return rslt;
    }

    @RequestMapping(value = "/funds/{fundId}/regions", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public List<RiskEntity> getRegionRiskList(@PathVariable("fundId") String aFundId) {
        ArrayList<RiskEntity> rslt = new ArrayList<RiskEntity>();
        if("1".equalsIgnoreCase(aFundId)) {
            rslt.add(createEntityRisk("AF"));
            rslt.add(createEntityRisk("NA"));
//            rslt.add(createEntityRisk("AS"));
            rslt.add(createEntityRisk("EU"));
        }else {

//            rslt.add(createEntityRisk("AF"));
//            rslt.add(createEntityRisk("NA"));
            rslt.add(createEntityRisk("AS"));
            rslt.add(createEntityRisk("EU"));
        }
        return rslt;
    }

    @RequestMapping(value = "/funds/{fundId}/regions/{regionId}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public RiskEntity getRegionRisk(@PathVariable("fundId") String aFundId, @PathVariable("regionId") String aRegionId) {
        // Mocked data
        return createEntityRisk("EU");
    }

    @RequestMapping(value = "/funds/{fundId}/regions/{regionId}/countries", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public List<RiskEntity> getCountriesRiskList(@PathVariable("fundId") String aFundId) {
        // Mocked data
        ArrayList<RiskEntity> result = new ArrayList<RiskEntity>();
        result.add(createEntityRisk("GMY"));
        result.add(createEntityRisk("FRA"));
        result.add(createEntityRisk("ESP"));
        result.add(createEntityRisk("IRL"));
        return result;
    }

    @RequestMapping(value = "/funds/{fundId}/countries/{countryId}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public RiskEntity getCountryRisk(@PathVariable("fundId") String aFundId, @PathVariable("countryId") String aCountryId) {
        // Mocked data
        return createEntityRisk("GMY");
    }


    private RiskEntity createEntityRisk(String anEntityId) {
        RiskEntity result = new RiskEntity();
        result.setFund(new LegalFund("1", "Fund1"));
        result.setEntityId(anEntityId);
        result.setLow(new Risk(25.0, 345, 12, 4));
        result.setMedium(new Risk(50.0, 5677, 82, 5));
        result.setHigh(new Risk(25.0, 35, 6, 1));
        return result;
    }
    
    private LegalFund createLegalFund(String anId, String aName) {
    	LegalFund rslt = new LegalFund(anId, aName);
    	return rslt;
    }
}
