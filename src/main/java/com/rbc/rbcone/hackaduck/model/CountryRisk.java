package com.rbc.rbcone.hackaduck.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * CountryRisk represents the risk distribution of a specific legal fund for a
 * specific country.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountryRisk {
	
    /**
     * The country ISO code.
     */
	private String countryCode;

    /**
     * The country name for human read.
     */
	private String countryName;
	
    /**
     * Risk information about the low risk category.
     */
	private Risk low;
	
    /**
     * Risk information about the medium risk category.
     */
	private Risk medium;
	
    /**
     * Risk information about the high risk category.
     */
	private Risk high;

}
