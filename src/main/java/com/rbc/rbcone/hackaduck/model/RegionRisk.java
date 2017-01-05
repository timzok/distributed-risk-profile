package com.rbc.rbcone.hackaduck.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class RegionRisk {
	
    /**
     * The region code.
     */
	private String regionCode;

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
	
	public void setRisk(Risk aRisk, String aRiskLevel) {
		if ("L".equals(aRiskLevel)) {
			low = aRisk;
		} else if ("M".equals(aRiskLevel)) {
			medium = aRisk;
		} else if ("H".equals(aRiskLevel)) {
			high = aRisk;
		} 
	}
	
	public void fillMissingRisks() {
		if (low==null) {
			low = new Risk(0, 0, 0);
		} 
		if (medium==null) {
			medium = new Risk(0, 0, 0);
		} 
		if (high==null) {
			high = new Risk(0, 0, 0);
		} 
		
	}
	
}
