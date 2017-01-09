package com.rbc.rbcone.hackaduck.model.incoming.service;

import java.util.List;

import com.rbc.rbcone.hackaduck.model.incoming.CountryRiskDB;
import com.rbc.rbcone.hackaduck.model.incoming.RegionRiskDB;
import com.rbc.rbcone.hackaduck.model.incoming.SaraEntityDB;

public interface BusinessDataService {

	public void feedBusinessDataForRegions();
	
	public void feedBusinessDataForCountries();

	List<RegionRiskDB> findRegionLevelRelationByFundId(String aFundId);

	List<CountryRiskDB> findCountryRiskByFundAndRegion(String fundId, String regionCode);
	
	List<CountryRiskDB> findTopXCountryRiskByFundAndRegion(String fundId, String regionCode, int topX);

	List<CountryRiskDB> findCountryRiskByFundAndCountry(String fundId, String countryCode);

	List<SaraEntityDB> findByFundAndDomicilationAndRiskCategory(String aFundId, String aCountryId, String rad);

	public void feedBusinessDataForSaraEntities();

}
