package com.crafters.DataService.exceptions;

import org.springframework.security.access.AccessDeniedException;

public class AccessDeniedExceptionHandle extends AccessDeniedException {
    public AccessDeniedExceptionHandle(String msg) {
        super(msg);
    }

    public AccessDeniedExceptionHandle(String msg, Throwable cause) {
        super(msg, cause);
    }
}
