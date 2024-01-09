package com.cbh.exceptionservice.exception;

import org.springframework.http.HttpStatus;

import com.cbh.exceptionservice.globalexception.GenericException;

/**
 * Exception thrown when there is an issue fetching invoices, and they are not found.
 * Inherits from {@link GenericException}.
 *
 * @see GenericException
 */
public class FetchInvoicesException extends GenericException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a FetchInvoicesException with the specified field name and message.
     *
     * @param fieldName The name of the field associated with the exception.
     * @param message   A descriptive message providing more information about the exception.
     */
    public FetchInvoicesException(String fieldName, String message) {
        super(fieldName, HttpStatus.NOT_FOUND, message);
    }
}

