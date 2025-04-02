package com.example.improvedscheduler.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateRequestDto {

    private String username;

    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String oldPassword;

    private String newPassword;

}
