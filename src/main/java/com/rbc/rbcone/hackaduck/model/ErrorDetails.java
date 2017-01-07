package com.rbc.rbcone.hackaduck.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ErrorDetails provides additional information about an error that occurred
 * during the call of a REST service. Typically ErrorDetails will be returned 
 * as response to a REST service call under a JSON format.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails implements Serializable {

	private String errorType;
	private String message;
	private int statusCode;
	
}
