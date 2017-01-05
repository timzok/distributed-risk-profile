package com.rbc.rbcone.hackaduck.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Peps represents a Political Exposed Person.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Peps {
	/**
	 * The person first name.
	 */
	private String firstName;
	
	/**
	 * The person last name.
	 */
	private String lastName;
	
	/**
	 * The person role (like shareholder, administrator).
	 */
	private String role;
	
	/**
	 * The person country ISO code.
	 */
	private String country;
}
