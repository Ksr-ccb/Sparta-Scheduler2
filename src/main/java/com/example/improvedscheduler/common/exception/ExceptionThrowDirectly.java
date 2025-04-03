package com.example.improvedscheduler.common.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class ExceptionThrowDirectly extends ResponseStatusException {
    public ExceptionThrowDirectly(HttpStatus status, String message) {
        super(status, message);
    }
}

