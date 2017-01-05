package com.rbc.rbcone.hackaduck.model.incoming;

import javax.persistence.Entity;
import javax.persistence.Id;

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
	public void setId(String id) {
		this.id = id;
	}
	public String getDomiciliation() {
		return domiciliation;
	}
	public void setDomiciliation(String domiciliation) {
		this.domiciliation = domiciliation;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
}

