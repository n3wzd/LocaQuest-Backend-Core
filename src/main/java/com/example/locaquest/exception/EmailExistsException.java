package com.example.locaquest.exception;

public class EmailExistsException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    public EmailExistsException(String message) {
        super(message);
    }

    public EmailExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
