package com.example.improvedscheduler.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ExceptionThrowDirectly {
    public ResourceNotFoundException(String message) {

        super(HttpStatus.NOT_FOUND, message);
    }
}
