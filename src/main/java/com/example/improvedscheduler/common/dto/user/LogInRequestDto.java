package com.example.improvedscheduler.common.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LogInRequestDto {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private final String password;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    private final String email;

    public LogInRequestDto(String password, String email) {
        this.password = password;
        this.email = email;
    }
}
