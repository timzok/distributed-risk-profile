package com.rbc.rbcone.hackaduck.model.incoming;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonSetter;

@Entity
public class AccountHolder {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	public String accountHolderId;
	
	public String residenceCode;
	public String name;
	public String type;
	public String nature;
	public String distributionType;
	public String rad;
	public String assetValue;
	public String relationType;
	public String accountNum;
	
	
	@ManyToMany(fetch = FetchType.EAGER,mappedBy="accountHolders")
	public Set<Fund> funds;

	@ManyToMany(fetch = FetchType.EAGER, targetEntity = Fund.class)
	@JoinTable(name="ACCOUNTSHOLDER_PEPS", 
			   joinColumns=@JoinColumn(name="account_holder_id",referencedColumnName="ID"),
			   inverseJoinColumns=@JoinColumn(name="peps_id",referencedColumnName="ID")
	)
	public Set<Peps> peps;
	
	public String getAccountHolderId() {
		return accountHolderId;
	}
	
	@JsonSetter("accountHolderId")
	public void setAccountHolderId(String accountHolderId) {
		this.accountHolderId = accountHolderId;
	}
	public String getResidenceCode() {
		return residenceCode;
	}
	
	@JsonSetter("residenceCode")
	public void setResidenceCode(String residenceCode) {
		this.residenceCode = residenceCode;
	}
	public String getName() {
		return name;
	}
	
	@JsonSetter("name")
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNature() {
		return nature;
	}
	
	@JsonSetter("natue")
	public void setNature(String nature) {
		this.nature = nature;
	}
	public String getDistributionType() {
		return distributionType;
	}
	
	@JsonSetter("distributionType")
	public void setDistributionType(String distributionType) {
		this.distributionType = distributionType;
	}
	public String getRad() {
		return rad;
	}
	
	@JsonSetter("rad")
	public void setRad(String rad) {
		this.rad = rad;
	}
	public String getAssetValue() {
		return assetValue;
	}
	
	@JsonSetter("assetValue")
	public void setAssetValue(String assetValue) {
		this.assetValue = assetValue;
	}
	public String getRelationType() {
		return relationType;
	}
	
	@JsonSetter("relationType")
	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}
	public String getAccountNum() {
		return accountNum;
	}
	
	@JsonSetter("accountNum")
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	public Set<Peps> getPeps() {
		return peps;
	}
	
	@JsonSetter("Peps")
	public void setPeps(Set<Peps> peps) {
		this.peps = peps;
	}

	public Set<Fund> getFunds() {
		return funds;
	}

	public void setFunds(Set<Fund> funds) {
		this.funds = funds;
	}
	
}
