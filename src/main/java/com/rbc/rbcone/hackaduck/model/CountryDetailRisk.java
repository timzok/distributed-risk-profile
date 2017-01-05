package com.rbc.rbcone.hackaduck.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * CountryDetailRisk is similar to {@link CountryRisk} with additional 
 * information about the fund and region.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountryDetailRisk extends CountryRisk {
	
    /**
     * The fund number.
     */
	private String fundId;

    /**
     * The fund name.
     */
	private String fundName;

	/**
	 * The country code.
	 */
	private String countryCode;

	/**
	 * The country name.
	 */
	private String countryName;

}
