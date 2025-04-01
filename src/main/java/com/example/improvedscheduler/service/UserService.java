package com.example.improvedscheduler.service;

import com.example.improvedscheduler.dto.UserResponseDto;

public interface UserService {
    UserResponseDto signUp(String username, String password, String email);
    UserResponseDto login(String email, String password);
}
