package com.example.locaquest.exception;

public class EmailNotExistsException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    public EmailNotExistsException(String message) {
        super(message);
    }

    public EmailNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
