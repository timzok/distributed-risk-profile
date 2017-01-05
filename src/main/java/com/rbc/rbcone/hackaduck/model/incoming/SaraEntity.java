package com.rbc.rbcone.hackaduck.model.incoming;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonSetter;

@Entity
public class SaraEntity {
	
	@Id
	public String id; 				// fc-bp-id
	public String residenceCode;	// fc-residence-code
	public String name;				// fc-name
	public String nature;			//fc-nature
	public String type;				//fc-type
	public String distributionType; // fc-distriutionType
	
	public String getId() {
		return id;
	}
	@JsonSetter("fc-bp-id")
	public void setId(String id) {
		this.id = id;
	}
	public String getResidenceCode() {
		return residenceCode;
	}
	
	@JsonSetter("fc-residence-code")
	public void setResidenceCode(String residenceCode) {
		this.residenceCode = residenceCode;
	}
	public String getName() {
		return name;
	}
	
	@JsonSetter("fc-name")
	public void setName(String name) {
		this.name = name;
	}
	public String getNature() {
		return nature;
	}
	@JsonSetter("fc-nature")
	public void setNature(String nature) {
		this.nature = nature;
	}
	public String getType() {
		return type;
	}
	
	@JsonSetter("fc-type")
	public void setType(String type) {
		this.type = type;
	}
	public String getDistributionType() {
		return distributionType;
	}
	@JsonSetter("fc-distriutionType")
	public void setDistributionType(String distributionType) {
		this.distributionType = distributionType;
	}
	
	public String toString(){
		
		return this.getId()+"-"+this.getName()+"-"+this.getDistributionType()+"-"+this.getNature()+"-"+this.getResidenceCode()+"-"+this.getType();
		
	}
	
}

