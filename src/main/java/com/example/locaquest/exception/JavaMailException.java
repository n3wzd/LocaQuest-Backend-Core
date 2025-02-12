package com.example.locaquest.exception;

public class JavaMailException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    public JavaMailException(String message) {
        super(message);
    }

    public JavaMailException(String message, Throwable cause) {
        super(message, cause);
    }
}
