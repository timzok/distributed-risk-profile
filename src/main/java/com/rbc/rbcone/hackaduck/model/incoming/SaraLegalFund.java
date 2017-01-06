package com.rbc.rbcone.hackaduck.model.incoming;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonSetter;

@Entity
public class SaraLegalFund {
	
	@Id
	String id; 			 	//fc-lf-id
	String domiciliation; 	// fc-lf-domiciliation
	String name;			// fc-lf-name
	String regionId;		// fc-region-id
	
	public String getId() {
		return id;
	}
	
	@JsonSetter("fc-lf-id")
	public void setId(String id) {
		this.id = id;
	}
	public String getDomiciliation() {
		return domiciliation;
	}
	
	@JsonSetter("fc-lf-domiciliation")
	public void setDomiciliation(String domiciliation) {
		this.domiciliation = domiciliation;
	}
	public String getName() {
		return name;
	}
	
	@JsonSetter("fc-lf-name")
	public void setName(String name) {
		this.name = name;
	}
	public String getRegionId() {
		return regionId;
	}
	
	@JsonSetter("fc-region-id")
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	
	public String toString()
	{
		return getId()+"-"+getDomiciliation()+"-"+getName()+"-"+getRegionId();
	}
}

