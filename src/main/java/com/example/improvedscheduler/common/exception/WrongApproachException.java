package com.example.improvedscheduler.common.exception;

import org.springframework.http.HttpStatus;

public class WrongApproachException extends ExceptionThrowDirectly {
    public WrongApproachException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}