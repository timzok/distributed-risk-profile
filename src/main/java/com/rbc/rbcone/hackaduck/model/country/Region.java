package com.rbc.rbcone.hackaduck.model.country;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;

import com.fasterxml.jackson.annotation.JsonSetter;

@Entity
public class Region {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int id;
	
	public String name;
	
//	fetch = FetchType.LAZY,
	// @OneToMany( fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE }, mappedBy = "region")
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL},mappedBy="region")
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
