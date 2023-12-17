package com.crafters.DataService.exceptions;

public class ValidationFailureException extends RuntimeException{

    public ValidationFailureException(String message) {
        super(message);
    }

    public ValidationFailureException(Throwable cause) {
        super(cause);
    }
}
