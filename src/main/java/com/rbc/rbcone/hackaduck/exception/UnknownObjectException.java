package com.rbc.rbcone.hackaduck.exception;

/**
 * UnknownObjectException is thrown when the application is unable to find an
 * object referenced in a REST query.
 */
public class UnknownObjectException extends Exception {

	public UnknownObjectException(String aMessage) {
		super(aMessage);
	}
	
}
