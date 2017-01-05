package com.rbc.rbcone.hackaduck.model.incoming;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonSetter;

@Entity
public class SaraRelation {

	@Id
	public int 	  id;
	
	public String relationId;  	//fc-relationId
	public String bpId;			//fc-bp-id
	public String lfId;			//fc-lf-id
	public String accId;		//fc-acc-id
	public String rad;			//fc-rad
	public String relationType;	//fc-relationType
	public String nbAcc;		//fi-nb-acc
	public String assetValue;	//fde-asset-value
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getRelationId() {
		return relationId;
	}
	
	@JsonSetter("fc-relationId")
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	public String getBpId() {
		return bpId;
	}
	@JsonSetter("fc-bp-id")
	public void setBpId(String bpId) {
		this.bpId = bpId;
	}
	public String getLfId() {
		return lfId;
	}
	@JsonSetter("fc-lf-id")
	public void setLfId(String lfId) {
		this.lfId = lfId;
	}
	public String getAccId() {
		return accId;
	}
	
	@JsonSetter("fc-acc-id")
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public String getRad() {
		return rad;
	}
	
	@JsonSetter("fc-rad")
	public void setRad(String rad) {
		this.rad = rad;
	}
	public String getRelationType() {
		return relationType;
	}

	@JsonSetter("fc-relationType")
	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}
	public String getNbAcc() {
		return nbAcc;
	}
	
	@JsonSetter("fi-nb-acc")
	public void setNbAcc(String nbAcc) {
		this.nbAcc = nbAcc;
	}
	public String getAssetValue() {
		return assetValue;
	}
	
	@JsonSetter("fde-asset-value")
	public void setAssetValue(String assetValue) {
		this.assetValue = assetValue;
	}
	
}
