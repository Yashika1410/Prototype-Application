package com.crafters.DataService.exceptions;

public class JWTExpirationException extends RuntimeException {
    public JWTExpirationException(String message) {
        super(message);
    }

    public JWTExpirationException(String message, Throwable cause) {
        super(message, cause);
    }
}
