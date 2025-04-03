package com.example.improvedscheduler.exception;

import org.springframework.http.HttpStatus;

public class WrongApproachException extends ExceptionThrowDirectly {
    public WrongApproachException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}