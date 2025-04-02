package com.example.improvedscheduler.service;

import com.example.improvedscheduler.dto.user.UserResponseDto;
import jakarta.validation.constraints.NotBlank;

public interface UserService {
    UserResponseDto signUp(String username, String password, String email);
    UserResponseDto login(String email, String password);

    void updatePassword(Long id, String username,
                        @NotBlank(message = "현재 비밀번호를 입력해주세요.") String oldPassword,
                        String newPassword);

}
