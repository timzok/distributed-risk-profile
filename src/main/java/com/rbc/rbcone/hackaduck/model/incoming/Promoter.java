package com.rbc.rbcone.hackaduck.model.incoming;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonSetter;

@Entity
public class Promoter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="PROMOTER_ID")
	public int id;
	
	
	
	public String saraRegion;
	
	public String name;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL},mappedBy="promoter")
	public Set<Fund> funds;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	@JsonSetter("name")
	public void setName(String name) {
		this.name = name;
	}
	public Set<Fund> getFunds() {
		return funds;
	}
	
	@JsonSetter("Funds")
	public void setFunds(Set<Fund> funds) {
		this.funds = funds;
	}
	public String getSaraRegion() {
		return saraRegion;
	}
	
	@JsonSetter("saraRegion")
	public void setSaraRegion(String saraRegion) {
		this.saraRegion = saraRegion;
	}
	
}
