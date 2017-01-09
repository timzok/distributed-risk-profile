package com.rbc.rbcone.hackaduck.rest;

import com.rbc.rbcone.hackaduck.exception.UnknownObjectException;
import com.rbc.rbcone.hackaduck.model.*;
import com.rbc.rbcone.hackaduck.model.incoming.*;
import com.rbc.rbcone.hackaduck.model.incoming.repository.SaraLegalFundRepository;
import com.rbc.rbcone.hackaduck.model.incoming.repository.SaraPepsRepository;
import com.rbc.rbcone.hackaduck.model.incoming.repository.SaraRelationRepository;
import com.rbc.rbcone.hackaduck.model.incoming.service.BusinessDataService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
public class RiskProfileResource {
    // Mocked funds
    private static final Logger log = LoggerFactory.getLogger(RiskProfileResource.class);
    private List<LegalFund> mockedFunds;
    private HashMap<String, List<RegionAndCountriesRisk>> fundIdToMockedRegionRisksMap;

    private static boolean IS_MOCKED = false;
    private HashMap<String, Double[]> fundIdToGlobalAssetPerRiskCategorykMap = new HashMap<String, Double[]>();

    @Autowired
    private SaraLegalFundRepository saraLegalFundRepo;
    @Autowired
    private SaraRelationRepository saraRelationRepo;
    @Autowired
    private SaraPepsRepository saraPepsRepo;

    @Autowired
    private BusinessDataService businessDataService;

    public RiskProfileResource() {
        //if (IS_MOCKED) //{
        // Mocked data initialization
        MockGenerator generator = new MockGenerator();
        fundIdToMockedRegionRisksMap = new HashMap<String, List<RegionAndCountriesRisk>>();
        mockedFunds = generator.generateLegalFunds();
        for (LegalFund mockedFund : mockedFunds) {
            List<RegionAndCountriesRisk> mockedRegionRisks = generator.generateWorldwideRiskData();
            fundIdToMockedRegionRisksMap.put(mockedFund.getId(), mockedRegionRisks);
        }
        //}
    }

    @RequestMapping(value = "/switch2Mock", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public boolean switch2Mock() {

        IS_MOCKED = true;
        return true;
    }

    @RequestMapping(value = "/switch2Datas", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public boolean switch2Data() {

        IS_MOCKED = false;
        return true;
    }

    @RequestMapping(value = "/funds", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public List<LegalFund> getFundList() {

        log.info("GET FUND LIST");

        if (IS_MOCKED) {
            return mockedFunds;
        } else {
            ArrayList<LegalFund> localFunds = new ArrayList<LegalFund>();
            for (SaraLegalFund cse : saraLegalFundRepo.findAll()) {
                localFunds.add(new LegalFund(cse.getId(), cse.getName()));
            }
            return localFunds;
        }
    }

    @RequestMapping(value = "/funds/{fundId}/regions", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public RegionsRisk getRegionRiskList(@PathVariable("fundId") String aFundId) {

        log.info("getRegionRiskList:" + "fund id: " + aFundId);

        if (IS_MOCKED) {
            LegalFund targetFund = findLegalFund(aFundId);
            List<RegionAndCountriesRisk> regionAndCountriesRisksList = findLegalFundWorldwideRisks(aFundId);

            RegionsRisk rslt = new RegionsRisk();
            rslt.setFundId(aFundId);
            rslt.setFundName(targetFund.getName());

            for (RegionAndCountriesRisk regionAndCountriesRisks : regionAndCountriesRisksList) {
                rslt.getRegions().add(regionAndCountriesRisks.getRegionRisk());
            }

            return rslt;
        } else {
            ;

            RegionsRisk rslt = new RegionsRisk();
            // LEM / MATETAM RATET / NA  / H / 0,0 / 1
            // through the service layer
            List<RegionRiskDB> riskRegionDbLisg = businessDataService.findRegionLevelRelationByFundId(aFundId);

            boolean first = true;

            for (RegionRiskDB currentRiskRegion : riskRegionDbLisg) {

                if (first) {
                    rslt.setFundId(currentRiskRegion.getFundId()); //LEM
                    rslt.setFundName(currentRiskRegion.getFundName()); //MATETAM RATET
                    first = false;
                }

                String regionCode = currentRiskRegion.getRegionId(); //NA

                RegionRisk regionRisk = rslt.getRegionRisk(regionCode);
                if (regionRisk == null) {
                    regionRisk = new RegionRisk();
                    regionRisk.setRegionCode(regionCode);
                    rslt.getRegions().add(regionRisk);
                }

                String riskCategory = currentRiskRegion.getRad(); //H
                Risk risk = new Risk(currentRiskRegion.getCountEntity(), currentRiskRegion.getSumAssetValue(), 0d);         //1 - 0,0
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

            // Low = 25
            // Medium = 25
            // High = 50

            for (RegionRisk region : rslt.getRegions()) {
                region.getLow().setGlobalAssetValue(globalAssetValueLowCategory);
                region.getMedium().setGlobalAssetValue(globalAssetValueMediumCategory);
                region.getHigh().setGlobalAssetValue(globalAssetValueHighCategory);
            }
            return rslt;
        }
    }

    @Deprecated
    @RequestMapping(value = "/funds2/{fundId}/regions", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public RegionsRisk getRegionRiskList2(@PathVariable("fundId") String aFundId) {
        if (IS_MOCKED) {
            LegalFund targetFund = findLegalFund(aFundId);
            List<RegionAndCountriesRisk> regionAndCountriesRisksList = findLegalFundWorldwideRisks(aFundId);

            RegionsRisk rslt = new RegionsRisk();
            rslt.setFundId(aFundId);
            rslt.setFundName(targetFund.getName());

            for (RegionAndCountriesRisk regionAndCountriesRisks : regionAndCountriesRisksList) {
                rslt.getRegions().add(regionAndCountriesRisks.getRegionRisk());
            }

            return rslt;
        } else {
            RegionsRisk rslt = new RegionsRisk();

            // select f.id as fundId, f.name, g.name as regionId, r.rad, sum(r.asset_Value), count(distinct e.id)
            // from country c, region g, sara_legal_fund f, sara_entity e, sara_relation r
            // where f.id = ? and r.lf_id = f.id and r.bp_id = e.id
            // and e.residence_code = c.type and c.region_id = g.id group by g.id, r.rad

            // LEM / MATETAM RATET / NA  / H / 0,0 / 1

            //TODO this kind of mapping should be substituted with a proper entity, because it is VERY dangerous and not OOP.
            Object[] rsRows = saraRelationRepo.findRegionLevelRelations(aFundId);

            for (int i = 0; i < rsRows.length; i++) {
                Object[] rsRow = (Object[]) rsRows[i];

                if (i == 0) {
                    rslt.setFundId((String) rsRow[0]); //LEM
                    rslt.setFundName((String) rsRow[1]); //MATETAM RATET
                }

                String regionCode = (String) rsRow[2]; //NA
                RegionRisk regionRisk = rslt.getRegionRisk(regionCode);
                if (regionRisk == null) {
                    regionRisk = new RegionRisk();
                    regionRisk.setRegionCode(regionCode);
                    rslt.getRegions().add(regionRisk);
                }

                String riskCategory = (String) rsRow[3]; //H
                Risk risk = new Risk(((BigInteger) rsRow[5]).intValue(), (double) rsRow[4], 0d);         //1 - 0,0
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

    private int calculateUniqueRandom(List<Integer> existingList) {
        int randomI = (int) (Math.random() * (5)) + 1;
        if (existingList == null || existingList.size() < 1) return randomI;

        for (int existing : existingList) {
            if (existing == randomI) return calculateUniqueRandom(existingList);
        }
        return randomI;
    }

    @RequestMapping(value = "/funds/{fundId}/regions/{regionId}/countries", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public CountriesRisk getCountriesRiskList(@PathVariable("fundId") String aFundId, @PathVariable("regionId") String aRegionCode) {
        log.info("getCountriesRiskList:" + "fund id: " + aFundId + " region id: " + aRegionCode);

        //Region id is the code here, not the real regionId.
        if (IS_MOCKED) {
            // Mocked data
            LegalFund targetFund = findLegalFund(aFundId);
            List<RegionAndCountriesRisk> regionAndCountriesRisksList = findLegalFundWorldwideRisks(aFundId);

            RegionAndCountriesRisk targetRegionCountriesRiskList = null;
            for (RegionAndCountriesRisk regionAndCountriesRisks : regionAndCountriesRisksList) {
                if (regionAndCountriesRisks.getRegionRisk().getRegionCode().equalsIgnoreCase(aRegionCode)) {
                    targetRegionCountriesRiskList = regionAndCountriesRisks;
                    break;
                }
            }

            CountriesRisk rslt = new CountriesRisk();
            rslt.setFundId(aFundId);
            rslt.setFundName(targetFund.getName());
            rslt.setRegionCode(aRegionCode);
            rslt.setCountries(targetRegionCountriesRiskList.getCountryRiskList());
            return rslt;
        } else {


            CountriesRisk rslt = new CountriesRisk();

            List<CountryRiskDB> counryRiskDbList = businessDataService.findCountryRiskByFundAndRegion(aFundId, aRegionCode.toUpperCase());

            boolean first = true;

            for (CountryRiskDB countryRiskDB : counryRiskDbList) {

                if (first) {
                    rslt.setRegionCode(aRegionCode);
                    rslt.setFundId(aFundId);
                    rslt.setFundName(countryRiskDB.getFundName());
                    first = false;
                }

                String countryCode = countryRiskDB.getCountryCode();
                CountryRisk countryRisk = rslt.getCountryRisk(countryCode);

                if (countryRisk == null) {
                    countryRisk = new CountryRisk();
                    countryRisk.setCountryCode(countryCode);
                    countryRisk.setCountryName(countryRiskDB.getCountryName());
                    rslt.getCountries().add(countryRisk);
                }

                String riskCategory = countryRiskDB.getRad();
                Risk risk = new Risk(countryRiskDB.getCountEntity(), countryRiskDB.getSumAssetValue(), 0d);
                countryRisk.setRisk(risk, riskCategory);
            }

		/*double globalAssetValueLowCategory = 0.0d;
		double globalAssetValueMediumCategory = 0.0d;
		double globalAssetValueHighCategory = 0.0d;
		
		for (CountryRisk country : rslt.getCountries()) {
			country.fillMissingRisks();
			
			globalAssetValueLowCategory += country.getLow().getAssetValue();
			globalAssetValueMediumCategory += country.getMedium().getAssetValue();
			globalAssetValueHighCategory += country.getHigh().getAssetValue();
		}
		
		
		fundIdToGlobalAssetPerRiskCategorykMap.put(aFundId, new Double[]{globalAssetValueLowCategory, globalAssetValueMediumCategory, globalAssetValueHighCategory});*/


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

    @Deprecated
    @RequestMapping(value = "/funds2/{fundId}/regions/{regionId}/countries", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public CountriesRisk getCountriesRiskList2(@PathVariable("fundId") String aFundId, @PathVariable("regionId") String aRegionId) {
        if (IS_MOCKED) {
            // Mocked data
            LegalFund targetFund = findLegalFund(aFundId);
            List<RegionAndCountriesRisk> regionAndCountriesRisksList = findLegalFundWorldwideRisks(aFundId);

            RegionAndCountriesRisk targetRegionCountriesRiskList = null;
            for (RegionAndCountriesRisk regionAndCountriesRisks : regionAndCountriesRisksList) {
                if (regionAndCountriesRisks.getRegionRisk().getRegionCode().equalsIgnoreCase(aRegionId)) {
                    targetRegionCountriesRiskList = regionAndCountriesRisks;
                    break;
                }
            }

            CountriesRisk rslt = new CountriesRisk();
            rslt.setFundId(aFundId);
            rslt.setFundName(targetFund.getName());
            rslt.setRegionCode(aRegionId);
            rslt.setCountries(targetRegionCountriesRiskList.getCountryRiskList());
            return rslt;
        } else {
            CountriesRisk rslt = new CountriesRisk();
            rslt.setRegionCode(aRegionId);

            // select f.id as fundId, f.name, c.type as countryCode, c.label as countryName, r.rad, sum(r.asset_Value), count(distinct e.id)
            // from country c, region g, sara_legal_fund f, sara_entity e, sara_relation r
            // where f.id = ? and g.name = ? and c.region_id = g.id and r.lf_id = f.id and r.bp_id = e.id and e.residence_code = c.type group by c.type, r.rad")

            Object[] rsRows = saraRelationRepo.findCountryLevelRelations(aFundId, aRegionId);

            for (int i = 0; i < rsRows.length; i++) {
                Object[] rsRow = (Object[]) rsRows[i];

                if (i == 0) {
                    rslt.setFundId((String) rsRow[0]);
                    rslt.setFundName((String) rsRow[1]);
                }

                String countryCode = (String) rsRow[2];
                CountryRisk countryRisk = rslt.getCountryRisk(countryCode);
                if (countryRisk == null) {
                    countryRisk = new CountryRisk();
                    countryRisk.setCountryCode(countryCode);
                    countryRisk.setCountryName((String) rsRow[3]);
                    rslt.getCountries().add(countryRisk);
                }

                String riskCategory = (String) rsRow[4];
                Risk risk = new Risk(((BigInteger) rsRow[6]).intValue(), (double) rsRow[5], 0d);
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
    public CountriesRisk getTopCountriesRiskList(@PathVariable("fundId") String aFundId, @PathVariable("regionId") String aRegionCode, @PathVariable("topCount") int aTopCount) {
        log.info("getTopCountriesRiskList:" + "fund id: " + aFundId + " region id: " + aRegionCode + "--" + aTopCount);
        if (IS_MOCKED) {
            // Mocked data
            CountriesRisk rslt = getCountriesRiskList(aFundId, aRegionCode);
            List<CountryRisk> countryRisk = rslt.getCountries();
            int endIndex = Math.min(countryRisk.size(), aTopCount);
            rslt.setCountries(countryRisk.subList(0, endIndex));
            return rslt;
        } else {


            //compared to getCountriesRiskList, the goal is to retrieve all top X High risk countries for wished fund and region
            // 3 parameters: nb of values requested + fundId + regionCode
            CountriesRisk rslt = new CountriesRisk();


            List<CountryRiskDB> countryRiskDbList = businessDataService.findTopXCountryRiskByFundAndRegion(aFundId, aRegionCode, aTopCount);

            boolean first = true;

            for (CountryRiskDB countryRiskDB : countryRiskDbList) {

                if (first) {
                    rslt.setRegionCode(aRegionCode);
                    rslt.setFundId(aFundId);
                    rslt.setFundName(countryRiskDB.getFundName());
                    first = false;
                }

                String countryCode = countryRiskDB.getCountryCode();
                CountryRisk countryRisk = rslt.getCountryRisk(countryCode);

                if (countryRisk == null) {
                    countryRisk = new CountryRisk();
                    countryRisk.setCountryCode(countryCode);
                    countryRisk.setCountryName(countryRiskDB.getCountryName());
                    rslt.getCountries().add(countryRisk);
                }

                Risk risk = new Risk(countryRiskDB.getCountEntity(), countryRiskDB.getSumAssetValue(), 0d);
                countryRisk.setRisk(risk, AbstractEntityRisk.HIGH_RISK_CATEGORY);
            }
		
		/*double globalAssetValueHighCategory = 0.0d;
		
		for (CountryRisk country : rslt.getCountries()) {
			
			country.fillHighRisk();
			
			//globalAssetValueLowCategory += country.getLow().getAssetValue();
			//globalAssetValueMediumCategory += country.getMedium().getAssetValue();
			globalAssetValueHighCategory += country.getHigh().getAssetValue();
		}
		fundIdToGlobalAssetPerRiskCategorykMap.put(aFundId, new Double[]{ globalAssetValueHighCategory});
		 */
            Double[] globalAssetPerRiskCategory = fundIdToGlobalAssetPerRiskCategorykMap.get(aFundId);

            for (CountryRisk country : rslt.getCountries()) {
                country.getHigh().setGlobalAssetValue(globalAssetPerRiskCategory[0]);
            }
            return rslt;
        }
    }

    @Deprecated
    @RequestMapping(value = "/funds2/{fundId}/regions/{regionId}/topcountries/{topCount}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public CountriesRisk getTopCountriesRiskList2(@PathVariable("fundId") String aFundId, @PathVariable("regionId") String aRegionId, @PathVariable("topCount") int aTopCount) {
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

            for (int i = 0; i < rsRows.length; i++) {
                Object[] rsRow = (Object[]) rsRows[i];

                if (i == 0) {
                    rslt.setFundId((String) rsRow[0]);
                    rslt.setFundName((String) rsRow[1]);
                }

                String countryCode = (String) rsRow[2];
                CountryRisk countryRisk = rslt.getCountryRisk(countryCode);
                if (countryRisk == null) {
                    countryRisk = new CountryRisk();
                    countryRisk.setCountryCode(countryCode);
                    countryRisk.setCountryName((String) rsRow[3]);
                    rslt.getCountries().add(countryRisk);
                }

                Risk risk = new Risk(((BigInteger) rsRow[5]).intValue(), (double) rsRow[4], 0d);
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

        log.info("getCountryRisk:" + "fund id: " + aFundId + " country id: " + aCountryId);

        if (IS_MOCKED) {
            // Mocked data
            LegalFund targetFund = findLegalFund(aFundId);
            List<RegionAndCountriesRisk> regionAndCountriesRisksList = findLegalFundWorldwideRisks(aFundId);

            CountryRisk targetCountryRisk = null;
            for (RegionAndCountriesRisk regionAndCountriesRisks : regionAndCountriesRisksList) {
                for (CountryRisk countryRisk : regionAndCountriesRisks.getCountryRiskList()) {
                    if (countryRisk.getCountryCode().equals(aCountryId)) {
                        targetCountryRisk = countryRisk;
                        break;
                    }
                }
                if (targetCountryRisk != null) {
                    break;
                }
            }

            CountryDetailRisk rslt = new CountryDetailRisk();
            rslt.setFundId(targetFund.getId());
            rslt.setCountryCode(aCountryId);
            rslt.fillMissingRisks();
            if (targetCountryRisk != null) {
                rslt.setFundName(targetFund.getName());
                rslt.setCountryName(targetCountryRisk.getCountryName());
                rslt.setLow(targetCountryRisk.getLow());
                rslt.setMedium(targetCountryRisk.getMedium());
                rslt.setHigh(targetCountryRisk.getHigh());
            }
            return rslt;
        } else {

            CountryDetailRisk rslt = new CountryDetailRisk();

            List<CountryRiskDB> counryRiskDbList = businessDataService.findCountryRiskByFundAndCountry(aFundId, aCountryId);

            boolean first = true;

            for (CountryRiskDB countryRisk : counryRiskDbList) {

                if (first) {
                    rslt.setFundId(countryRisk.getFundId());
                    rslt.setFundName(countryRisk.getFundName());
                    rslt.setCountryCode(countryRisk.getCountryCode());
                    rslt.setCountryName(countryRisk.getCountryName());
                    first = false;
                }

                String riskCategory = countryRisk.getRad();
                Risk risk = new Risk(countryRisk.getCountEntity(), countryRisk.getSumAssetValue(), 0d);
                rslt.setRisk(risk, riskCategory);

            }
    	
     	/*double globalAssetValueLowCategory = 0.0d;
		double globalAssetValueMediumCategory = 0.0d;
		double globalAssetValueHighCategory = 0.0d;

		rslt.fillMissingRisks();
 		globalAssetValueLowCategory = rslt.getLow().getAssetValue();
 		globalAssetValueMediumCategory = rslt.getMedium().getAssetValue();
 		globalAssetValueHighCategory = rslt.getHigh().getAssetValue();
		
    
		fundIdToGlobalAssetPerRiskCategorykMap.put(aFundId, new Double[]{ globalAssetValueLowCategory, globalAssetValueMediumCategory, globalAssetValueHighCategory});*/


            Double[] globalAssetPerRiskCategory = fundIdToGlobalAssetPerRiskCategorykMap.get(aFundId);

            rslt.fillMissingRisks();
            rslt.getLow().setGlobalAssetValue(globalAssetPerRiskCategory[0]);
            rslt.getMedium().setGlobalAssetValue(globalAssetPerRiskCategory[1]);
            rslt.getHigh().setGlobalAssetValue(globalAssetPerRiskCategory[2]);
            return rslt;
        }
    }

    @Deprecated
    @RequestMapping(value = "/funds2/{fundId}/countries/{countryId}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8"})
    public CountryDetailRisk getCountryRisk2(@PathVariable("fundId") String aFundId, @PathVariable("countryId") String aCountryId) {

        log.info("COUNTRY DETAIL RISK: " + aFundId + " - " + aCountryId);

        if (IS_MOCKED) {
            // Mocked data
            LegalFund targetFund = findLegalFund(aFundId);
            List<RegionAndCountriesRisk> regionAndCountriesRisksList = findLegalFundWorldwideRisks(aFundId);

            CountryRisk targetCountryRisk = null;
            for (RegionAndCountriesRisk regionAndCountriesRisks : regionAndCountriesRisksList) {
                for (CountryRisk countryRisk : regionAndCountriesRisks.getCountryRiskList()) {
                    if (countryRisk.getCountryCode().equals(aCountryId)) {
                        targetCountryRisk = countryRisk;
                        break;
                    }
                }
                if (targetCountryRisk != null) {
                    break;
                }
            }

            CountryDetailRisk rslt = new CountryDetailRisk();
            rslt.setFundId(targetFund.getId());
            rslt.setCountryCode(aCountryId);
            rslt.fillMissingRisks();
            if (targetCountryRisk != null) {
                rslt.setFundName(targetFund.getName());
                rslt.setCountryName(targetCountryRisk.getCountryName());
                rslt.setLow(targetCountryRisk.getLow());
                rslt.setMedium(targetCountryRisk.getMedium());
                rslt.setHigh(targetCountryRisk.getHigh());
            }
            return rslt;
        } else {
            CountryDetailRisk rslt = new CountryDetailRisk();
            rslt.setFundId(aFundId);
            rslt.setCountryCode(aCountryId);

            // select f.id as fundId, f.name, g.name as regionCode, c.type as countryCode, c.label as countryName, r.rad, sum(r.asset_Value), count(distinct e.id)
            // from country c, region g, sara_legal_fund f, sara_entity e, sara_relation r
            // where f.id = ? and c.type = ? and c.region_id = g.id and r.lf_id = f.id and r.bp_id = e.id and e.residence_code = c.type group by c.type, r.rad

            Object[] rsRows = saraRelationRepo.findCountryRelations(aFundId, aCountryId);

            for (int i = 0; i < rsRows.length; i++) {
                Object[] rsRow = (Object[]) rsRows[i];

                if (i == 0) {
                    rslt.setFundId((String) rsRow[0]);
                    rslt.setFundName((String) rsRow[1]);
                    rslt.setCountryCode((String) rsRow[3]);
                    rslt.setCountryName((String) rsRow[4]);
                }

                String riskCategory = (String) rsRow[5];
                Risk risk = new Risk(((BigInteger) rsRow[7]).intValue(), (double) rsRow[6], 0d);
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

        log.info("Get Legal entity Peps + " + aFundId + "-" + aCountryId + "-" + aRiskCategory);

        return getLegalEntityPepsHelper(aFundId, aCountryId, aRiskCategory);
    }


    @RequestMapping(value = "/funds/{fundId}/countries/{countryId}/legalEntitiesExport/rads/{rad}", method = RequestMethod.GET, produces = {"text/csv; charset=UTF-8"})
    public void getLegalEntityPepsCSV(@PathVariable("fundId") String aFundId, @PathVariable("countryId") String aCountryId, @PathVariable("rad") String aRiskCategory, HttpServletResponse aResponse) throws IOException, UnknownObjectException {

        log.info("Get Legal entity Peps CSV+ " + aFundId + "-" + aCountryId + "-" + aRiskCategory);

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

        if (aRiskCategory.toUpperCase().equals("MEDIUM")) aRiskCategory = "M";
        else if (aRiskCategory.toUpperCase().equals("LOW")) aRiskCategory = "L";
        else aRiskCategory = "H";

        log.info("aRiskCategory is changed to :" + aRiskCategory);

        if (IS_MOCKED) {
            // Mocked data
            LegalFund targetFund = findLegalFund(aFundId);
            CountryLegalEntityRisk rslt = new CountryLegalEntityRisk();
            rslt.setFundId(targetFund.getId());
            rslt.setFundName(targetFund.getName());
            rslt.setCountryCode(aCountryId);
            rslt.setRad(aRiskCategory);

            ArrayList<LegalEntity> entities = new ArrayList<LegalEntity>();
            entities.add(new LegalEntity("1", "LegalEntity1", "Other Financial Institution", "Legal", Arrays.asList(new Peps("1", "Toto", "Van Der Meulen", "Administrator", "LU"), new Peps("2", "Titi", "Dooren", "ShareHolder", "BE"))));
            entities.add(new LegalEntity("2", "LegalEntity2", "Other Financial Institution", "Legal", Arrays.asList(new Peps("3", "Tutu", "Vonckens", "ShareHolder", "FR"))));

            rslt.setLegalEntities(entities);
            return rslt;
        } else {
            CountryLegalEntityRisk rslt = new CountryLegalEntityRisk();
            rslt.setCountryCode(aCountryId);
            rslt.setRad(aRiskCategory);

            SaraLegalFund legalFund = saraLegalFundRepo.findOne(aFundId);
            if (legalFund == null) {
                throw new UnknownObjectException("No fund found identified by fund id '" + aFundId + "'");
            }
            rslt.setFundId(legalFund.getId());
            rslt.setFundName(legalFund.getName());

            List<SaraPeps> saraPepsList = saraPepsRepo.findPepsByFundAndCountryAndRiskCategory(aFundId, aCountryId, aRiskCategory);
            List<SaraEntityDB> saraEntityList = businessDataService.findByFundAndDomicilationAndRiskCategory(aFundId, aCountryId, aRiskCategory);
            //List<Object[]> saraEntityList = .findByFundAndDomicilationAndRiskCategory(aFundId, aCountryId, aRiskCategory);

            HashMap<String, LegalEntity> legalEntityIdToLegalEntityMap = new HashMap<String, LegalEntity>();
            String legalEntityId = null;
            LegalEntity legalEntity = null;
            String relationId = null;

            for (SaraEntityDB saraEntityDB : saraEntityList) {
                legalEntityId = saraEntityDB.getSaraEntityId();
                legalEntity = legalEntityIdToLegalEntityMap.get(legalEntityId);

                if (legalEntity == null) {
                    legalEntity = new LegalEntity(legalEntityId, saraEntityDB.getName(), saraEntityDB.getType(), saraEntityDB.getNature(), new ArrayList<Peps>());
                    rslt.getLegalEntities().add(legalEntity);
                    legalEntityIdToLegalEntityMap.put(legalEntityId, legalEntity);
                }

                relationId = saraEntityDB.getRelationId();
                for (SaraPeps saraPeps : saraPepsList) {
                    if (relationId.equals(saraPeps.getRelationId())) {
                        legalEntity.getPeps().add(new Peps(legalEntityId, saraPeps.getFirstName(), saraPeps.getLastName(), saraPeps.getRole(), saraPeps.getCountry()));
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

    private LegalFund findLegalFund(String aFundId) {
        for (LegalFund fund : mockedFunds) {
            if (fund.getId().equals(aFundId)) {
                return fund;
            }
        }
        return mockedFunds.get(0);
    }

    private List<RegionAndCountriesRisk> findLegalFundWorldwideRisks(String aFundId) {
        List<RegionAndCountriesRisk> rslt = fundIdToMockedRegionRisksMap.get(aFundId);
        if (rslt == null) {
            String legalFundId = mockedFunds.get(0).getId();
            rslt = fundIdToMockedRegionRisksMap.get(legalFundId);
        }
        return rslt;
    }
}
