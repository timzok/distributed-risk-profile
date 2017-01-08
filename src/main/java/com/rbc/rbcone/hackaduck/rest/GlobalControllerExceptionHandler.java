package com.rbc.rbcone.hackaduck.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.rbc.rbcone.hackaduck.exception.UnknownObjectException;
import com.rbc.rbcone.hackaduck.model.ErrorDetails;

/**
 * Global handler for the exceptions generated during REST service calls.
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {
	
	@ExceptionHandler(UnknownObjectException.class)
	@ResponseStatus(code=HttpStatus.BAD_REQUEST)
	public ErrorDetails handleUnknownObjectException(UnknownObjectException anException, HttpServletRequest aRequest) {
		ErrorDetails errorDetails = new ErrorDetails("Bad Request Error", anException.getMessage(), HttpStatus.BAD_REQUEST.value());
		anException.printStackTrace();
		return errorDetails;
	}
	
	@ExceptionHandler(Throwable.class)
	@ResponseStatus(code=HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorDetails handleThrowable(Throwable anException, HttpServletRequest aRequest) {
		ErrorDetails errorDetails = new ErrorDetails("Internal Server Error", "An unexpected error occurred. Error is of type " + anException.getClass().getSimpleName(), HttpStatus.INTERNAL_SERVER_ERROR.value());
		anException.printStackTrace();
		return errorDetails;
	}
}
