package com.rbc.rbcone.hackaduck.model.country;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonSetter;

@Entity
public class Region {
	
	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	public int id;
	
	public String name;
	
	@OneToMany(mappedBy="region")
	public Set<Country> country;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getName() {
		return name;
	}

	@JsonSetter("region")
	public void setName(String label) {
		this.name = label;
	}

	public Set<Country> getCountry() {
		return country;
	}

	@JsonSetter("countries")
	public void setCountry(Set<Country> country) {
		this.country = country;
	}

}
