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
public class SaraEntityDB {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	public int id;

	public String saraEntityId;		// fc-bp-id
	public String residenceCode;	// fc-residence-code
	public String name;				// fc-name
	public String nature;			// fc-nature
	public String type;	
	public String relationId;
	public String saraFundId;
	public String rad;
	
}
