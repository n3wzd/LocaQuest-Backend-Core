package com.example.locaquest.exception;

public class AlreadyVerifiedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AlreadyVerifiedException(String message) {
        super(message);
    }

    public AlreadyVerifiedException(String message, Throwable cause) {
        super(message, cause);
    }
}
