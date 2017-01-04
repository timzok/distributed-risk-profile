package com.rbc.rbcone.hackaduck.model.country;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonSetter;

@Entity
public class Country {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int countryId;

	@ManyToOne(cascade=CascadeType.MERGE)
	@JoinColumn(name="region_id")
	public Region region;
	
	
	public String type;
	public String label;
	public int getCountryId() {
		return countryId;
	}
	
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	public String getType() {
		return type;
	}
	
	@JsonSetter("FIELD2")
	public void setType(String type) {
		this.type = type;
	}
	public String getLabel() {
		return label;
	}
	
	@JsonSetter("FIELD3")
	public void setLabel(String label) {
		this.label = label;
	}
	
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	
}
