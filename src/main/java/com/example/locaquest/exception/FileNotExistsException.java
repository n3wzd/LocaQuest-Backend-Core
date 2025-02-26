package com.example.locaquest.exception;

public class FileNotExistsException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public FileNotExistsException() {
        super("");
    }
	
    public FileNotExistsException(String message) {
        super(message);
    }

    public FileNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
