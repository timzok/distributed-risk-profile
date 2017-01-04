package com.rbc.rbcone.hackaduck.model;

/**
 * EntityRisk represents the distribution of the risk of a specific legal fund
 * relative to an entity. That entity can either represents a geographical 
 * region or either a country.
 * @author user
 *
 */
public class EntityRisk {
	/** The legal fund of which its risk is described by this instance. */
	private LegalFund fund;
	/** The unique identifier of the entity (like a geographical region or like an ISO country code). */
	private String entityId;
	/** Information about the low risk category. */
	private Risk low;
	/** Information about the medium risk category. */
	private Risk medium;
	/** Information about the high risk category. */
	private Risk high;
	
	//**************************************************************************
	//**** Getters / Setters ***************************************************
	//**************************************************************************
	
	public LegalFund getFund() {
		return fund;
	}
	public void setFund(LegalFund fund) {
		this.fund = fund;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public Risk getLow() {
		return low;
	}
	public void setLow(Risk low) {
		this.low = low;
	}
	public Risk getMedium() {
		return medium;
	}
	public void setMedium(Risk medium) {
		this.medium = medium;
	}
	public Risk getHigh() {
		return high;
	}
	public void setHigh(Risk high) {
		this.high = high;
	}
	
	
	
}
