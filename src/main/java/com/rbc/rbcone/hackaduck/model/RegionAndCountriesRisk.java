package com.rbc.rbcone.hackaduck.model;

import java.util.ArrayList;

/**
 * RegionAndCountriesRisk is a data structure that holds a region risk and the
 * risk data of all the countries that compose that region.
 */
public class RegionAndCountriesRisk {

	private RegionRisk regionRisk;
	private ArrayList<CountryRisk> countryRiskList;
	
	public RegionAndCountriesRisk(String aRegionCode) {
		regionRisk = new RegionRisk();
		regionRisk.setRegionCode(aRegionCode);
		regionRisk.setLow(new Risk(0, 0.0, 0.0));
		regionRisk.setMedium(new Risk(0, 0.0, 0.0));
		regionRisk.setHigh(new Risk(0, 0.0, 0.0));
		countryRiskList = new ArrayList<CountryRisk>();
	}
	
	public void addCountryRisk(CountryRisk aCountryRisk) {
		countryRiskList.add(aCountryRisk);
		
		appendRisk(aCountryRisk.getLow(), regionRisk.getLow());
		appendRisk(aCountryRisk.getMedium(), regionRisk.getMedium());
		appendRisk(aCountryRisk.getHigh(), regionRisk.getHigh());
	}
	
	public RegionAndCountriesRisk consolidate() {
		for (CountryRisk countryRisk : countryRiskList) {
			if (countryRisk.getLow()!=null) {
				countryRisk.getLow().setGlobalAssetValue(regionRisk.getLow().getAssetValue());
				countryRisk.getMedium().setGlobalAssetValue(regionRisk.getMedium().getAssetValue());
				countryRisk.getHigh().setGlobalAssetValue(regionRisk.getHigh().getAssetValue());
			}
		}
		
		return this;
	}
	
	public RegionRisk getRegionRisk() {
		return regionRisk;
	}
	
	public ArrayList<CountryRisk> getCountryRiskList() {
		return countryRiskList;
	}
	
	private void appendRisk(Risk aRisk, Risk anAccumulatorRisk) {
		if (aRisk!=null) {
			anAccumulatorRisk.setInvestorCount(anAccumulatorRisk.getInvestorCount() + aRisk.getInvestorCount());
			anAccumulatorRisk.setAssetValue(anAccumulatorRisk.getAssetValue() + aRisk.getAssetValue());
			anAccumulatorRisk.setGlobalAssetValue(anAccumulatorRisk.getGlobalAssetValue() + aRisk.getGlobalAssetValue());
		}
	}
}
