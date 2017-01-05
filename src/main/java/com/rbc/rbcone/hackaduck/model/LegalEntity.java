package com.rbc.rbcone.hackaduck.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * LegalEntity represents a Legal Entity. A legal entity can for example be a
 * legal fund distributor.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LegalEntity {
	/**
	 * The legal entity name.
	 */
	private String name;

	/**
	 * The legal entity type (like Bank, Other financial institution).
	 */
	private String type;
	
	/**
	 * The legal entity nature (like Legal).
	 */
	private String nature;
	
	/**
	 * The persons that are in relationship with this legal entity.
	 */
	private List<Peps> peps;
}
