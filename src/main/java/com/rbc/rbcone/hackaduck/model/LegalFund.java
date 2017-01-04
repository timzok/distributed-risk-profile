package com.rbc.rbcone.hackaduck.model;

/**
 * LegalFund represents a legal fund.
 * @author user
 *
 */
public class LegalFund {
	/** The fund identifier. */
	private String id;
	/** The fund name. */
	private String name;
	
	/**
	 * Create a new LegalFund instance.
	 */
	public LegalFund() {
		
	}
	
	/**
	 * Create a new LegalFund instance and initialize it.
	 */
	public LegalFund(String anId, String aName) {
		id = anId;
		name = aName;
	}
	
	//**************************************************************************
	//**** Getters / Setters ***************************************************
	//**************************************************************************
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
