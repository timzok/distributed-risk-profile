package com.rbc.rbcone.hackaduck.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * CountryLegalEntityRisk provides information about the risk of the legal 
 * entities that are located in a specific country and in relation with a 
 * specific fund.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountryLegalEntityRisk {
	/**
	 * The legal fund number. 
	 */
	private String fundId;
	
	/**
	 * The legal fund name. 
	 */
	private String fundName;
	
	/**
	 * The legal entity location as an ISO country code.
	 */
	private String countryCode;
	
	/**
	 * The risk category: L (Low), M (Medium), H (High).
	 */
	private String rad;
	
	/**
	 * The list of the legal entities operating on a specific fund and located
	 * in a specific country.
	 */
	private List<LegalEntity> legalEntities = new ArrayList<LegalEntity>();
}
