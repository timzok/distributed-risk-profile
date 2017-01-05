package com.rbc.rbcone.hackaduck.model.incoming;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SaraEntity {
	
	@Id
	public String id; 				// fc-bp-id
	public String residenceCode;	// fc-residence-code
	public String name;				// fc-name
	public String nature;			//fc-nature
	public String type;				//fc-type
	public String distributionType; // fc-distriutionType
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getResidenceCode() {
		return residenceCode;
	}
	public void setResidenceCode(String residenceCode) {
		this.residenceCode = residenceCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNature() {
		return nature;
	}
	public void setNature(String nature) {
		this.nature = nature;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDistributionType() {
		return distributionType;
	}
	public void setDistributionType(String distributionType) {
		this.distributionType = distributionType;
	}
	
}


/*{"ttEntity": [
{
"fc-bp-id": "MIG-DLR-b442",
"fc-residence-code": "LU",
"fc-name": " ",
"fc-nature": "L",
"fc-type": "01",
"fc-distriutionType": "Segragated"
}
]}*/