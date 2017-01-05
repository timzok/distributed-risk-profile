package com.rbc.rbcone.hackaduck.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * RegionsRisk represents the distribution of the risk of a specific legal fund
 * for the different world regions.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegionsRisk {
	
    /**
     * The fund number.
     */
	private String fundId;

    /**
     * The fund name.
     */
	private String fundName;
	
    /**
     * Risk information about the medium risk category.
     */
	private List<RegionRisk> regions;

}
