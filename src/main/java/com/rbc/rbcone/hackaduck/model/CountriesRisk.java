package com.rbc.rbcone.hackaduck.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * CountriesRisk represents the distribution of the risk of a specific legal 
 * fund for all the countries of a same geographical region.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountriesRisk {
	
    /**
     * The fund number.
     */
	private String fundId;

    /**
     * The fund name.
     */
	private String fundName;

	/**
	 * The region code
	 */
	private String regionCode;
	
    /**
     * Risk information for each country.
     */
	private List<CountryRisk> countries = new ArrayList<CountryRisk>();

	
	public CountryRisk getCountryRisk(String aCountryCode) {
		for (CountryRisk countryRisk : countries) {
			if (countryRisk.getCountryCode().equals(aCountryCode)) {
				return countryRisk;
			}
		}
		return null;
	}
}
