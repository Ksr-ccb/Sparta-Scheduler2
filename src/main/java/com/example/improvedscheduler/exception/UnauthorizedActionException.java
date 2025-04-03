package com.example.improvedscheduler.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedActionException extends ExceptionThrowDirectly {
    public UnauthorizedActionException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
