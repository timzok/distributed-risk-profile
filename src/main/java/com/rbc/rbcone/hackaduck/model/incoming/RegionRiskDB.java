package com.rbc.rbcone.hackaduck.model.incoming;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RegionRiskDB {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	public int id;
	
	public String fundId;
	public String fundName;
	public String regionId;
	public String rad;
	public double sumAssetValue;
	public int countEntity;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFundId() {
		return fundId;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getRad() {
		return rad;
	}
	public void setRad(String rad) {
		this.rad = rad;
	}
	public double getSumAssetValue() {
		return sumAssetValue;
	}
	public void setSumAssetValue(double sumAssetValue) {
		this.sumAssetValue = sumAssetValue;
	}
	
	public int getCountEntity() {
		return countEntity;
	}
	public void setCountEntity(int countEntity) {
		this.countEntity = countEntity;
	}
	
	
}
