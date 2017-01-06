package com.rbc.rbcone.hackaduck.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * RegionRisk represents the distribution of the risk of a specific legal fund
 * for a specific region. The region is identified by its region code.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegionRisk extends AbstractEntityRisk {
	
    /**
     * The region code.
     */
	private String regionCode;
		
}
