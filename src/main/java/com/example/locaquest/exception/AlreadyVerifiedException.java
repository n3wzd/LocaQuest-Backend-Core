package com.example.locaquest.exception;

public class AlreadyVerifiedException extends RuntimeException {
    public AlreadyVerifiedException(String message) {
        super(message);
    }

    public AlreadyVerifiedException(String message, Throwable cause) {
        super(message, cause);
    }
}
