package com.rbc.rbcone.hackaduck.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractEntityRisk {

	public static final String LOW_RISK_CATEGORY = "L";
	public static final String MEDIUM_RISK_CATEGORY = "M";
	public static final String HIGH_RISK_CATEGORY = "H";
	
    /**
     * Risk information about the low risk category.
     */
	protected Risk low;
	
    /**
     * Risk information about the medium risk category.
     */
	protected Risk medium;
	
    /**
     * Risk information about the high risk category.
     */
	protected Risk high;
	
	
	
	public void setRisk(Risk aRisk, String aRiskLevel) {
		if (LOW_RISK_CATEGORY.equals(aRiskLevel)) {
			low = aRisk;
		} else if (MEDIUM_RISK_CATEGORY.equals(aRiskLevel)) {
			medium = aRisk;
		} else if (HIGH_RISK_CATEGORY.equals(aRiskLevel)) {
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
