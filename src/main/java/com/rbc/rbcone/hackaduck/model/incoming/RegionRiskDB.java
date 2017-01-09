package com.rbc.rbcone.hackaduck.model.incoming;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
	
}
