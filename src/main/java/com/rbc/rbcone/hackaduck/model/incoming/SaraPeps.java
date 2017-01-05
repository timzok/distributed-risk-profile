package com.rbc.rbcone.hackaduck.model.incoming;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonSetter;

@Entity
public class SaraPeps {

	@Id
	String pepsId;			// fc-pepsId
	
	String firstName;		// fc-firtName;
	String lastName;		// fc-lastName
	String role;			// fc-role
	String nationality; 	// fc-nationality
	String country;			// fc-country
	String relationId;  	// fc-relationId
	
	public String getPepsId() {
		return pepsId;
	}
	
	@JsonSetter("fc-pepsId")
	public void setPepsId(String pepsId) {
		this.pepsId = pepsId;
	}
	public String getFirstName() {
		return firstName;
	}
	
	@JsonSetter("fc-firtName")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	
	@JsonSetter("fc-lastName")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getRole() {
		return role;
	}
	
	@JsonSetter("fc-role")
	public void setRole(String role) {
		this.role = role;
	}
	public String getNationality() {
		return nationality;
	}
	
	@JsonSetter("fc-nationality")
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getCountry() {
		return country;
	}
	
	@JsonSetter("fc-country")
	public void setCountry(String country) {
		this.country = country;
	}
	
	
	public String getRelationId() {
		return relationId;
	}
	
	@JsonSetter("fc-relationId")
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	
	public String toString(){
		
		return 	getPepsId()+"-"+getFirstName()+"-"+getLastName()+"-"+getRole()+"-"+getNationality()+"-"+getCountry()+"-"+getRelationId();			// fc-pepsId
		
	}

}