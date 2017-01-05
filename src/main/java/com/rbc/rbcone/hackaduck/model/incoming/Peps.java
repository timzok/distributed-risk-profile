package com.rbc.rbcone.hackaduck.model.incoming;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonSetter;

@Entity
public class Peps {

	@Id
	@Column(name="ID")
	public String pepsId;
	
	public String firstName;
	public String lastName;
	public String role;
	public String nationality;
	public String country;
	
	@ManyToMany(mappedBy="peps")
	public Set<AccountHolder> accountHolders;

	public String getPepsId() {
		return pepsId;
	}
	
	@JsonSetter("pepsId")
	public void setPepsId(String pepsId) {
		this.pepsId = pepsId;
	}

	public String getFirstName() {
		return firstName;
	}

	@JsonSetter("firtName")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	@JsonSetter("lastName")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRole() {
		return role;
	}

	@JsonSetter("role")
	public void setRole(String role) {
		this.role = role;
	}

	public String getNationality() {
		return nationality;
	}

	@JsonSetter("nationality")
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getCountry() {
		return country;
	}

	@JsonSetter("country")
	public void setCountry(String country) {
		this.country = country;
	}

	public Set<AccountHolder> getAccountHolders() {
		return accountHolders;
	}

	public void setAccountHolders(Set<AccountHolder> accountHolders) {
		this.accountHolders = accountHolders;
	}
	
}
