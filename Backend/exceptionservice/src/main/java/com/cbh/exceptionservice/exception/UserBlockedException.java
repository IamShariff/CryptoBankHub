package com.cbh.exceptionservice.exception;

import org.springframework.http.HttpStatus;

import com.cbh.exceptionservice.globalexception.GenericException;

/**
 * Exception thrown when a user is blocked, resulting in a conflict HTTP status.
 * Inherits from {@link GenericException}.
 *
 * @see GenericException
 */
public class UserBlockedException extends GenericException {

	private static final long serialVersionUID = 1L;
	private static final HttpStatus httpStatusCode = HttpStatus.CONFLICT;

	/**
	 * Constructs a UserBlockedException with the specified field name and message.
	 *
	 * @param fieldName The name of the field associated with the exception.
	 * @param message   A descriptive message providing more information about the
	 *                  exception.
	 */
	public UserBlockedException(final String fieldName, final String message) {
		super(fieldName, httpStatusCode, String.format("%s", message));
	}
}
