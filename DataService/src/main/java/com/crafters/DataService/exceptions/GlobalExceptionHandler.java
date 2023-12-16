package com.crafters.DataService.exceptions;

import org.springframework.web.bind.annotation.ExceptionHandler;

//TODO: complete this
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public void handleEntityExistsException(IllegalArgumentException ex) {

    }
}
