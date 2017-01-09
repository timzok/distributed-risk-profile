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
public class CountryRiskDB {

	// select f.id as fundId, f.name, c.type as countryCode, c.label as countryName, r.rad, sum(r.asset_Value), count(distinct e.id) 
			// from country c, region g, sara_legal_fund f, sara_entity e, sara_relation r 
			// where f.id = ? and g.name = ? and c.region_id = g.id and r.lf_id = f.id and r.bp_id = e.id and e.residence_code = c.type group by c.type, r.rad")
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	public int id;
	public String fundId;
	public String fundName;
	public String countryCode;
	public String countryName;
	public String rad;
	public String regionId;
	//public String regionCode;
	public double sumAssetValue;
	public int countEntity;
	
	
}
