package com.example.improvedscheduler.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedActionException extends ExceptionThrowDirectly {
    public UnauthorizedActionException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
