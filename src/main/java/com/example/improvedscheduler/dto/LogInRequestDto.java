package com.example.improvedscheduler.dto;

import lombok.Getter;

@Getter
public class LogInRequestDto {
    private final String password;

    private final String email;

    public LogInRequestDto(String password, String email) {
        this.password = password;
        this.email = email;
    }
}
