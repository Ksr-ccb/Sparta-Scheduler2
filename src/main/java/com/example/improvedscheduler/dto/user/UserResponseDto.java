package com.example.improvedscheduler.dto.user;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 회원 가입 완료 후 반환하는 데이털르 담는 dto입니다.
 */
@Getter
public class UserResponseDto {
    private final Long id;

    private final String username;

    private final String email;

    public final LocalDateTime createDate;

    public final LocalDateTime updateDate;

    public UserResponseDto(Long id, String username, String email, LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
}
