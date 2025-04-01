package com.example.improvedscheduler.dto;

import lombok.Getter;

/**
 * 회원 가입 완료 후 반환하는 데이털르 담는 dto입니다.
 */
@Getter
public class UserResponseDto {
    private final Long id;

    private final String username;

    private final String email;

    public UserResponseDto(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
