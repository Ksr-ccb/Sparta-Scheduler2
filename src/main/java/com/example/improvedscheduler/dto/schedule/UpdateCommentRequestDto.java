package com.example.improvedscheduler.dto.schedule;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateCommentRequestDto {
    @NotBlank(message = "내용 입력은 필수입니다.")
    private final String contents;

    public UpdateCommentRequestDto(String contents) {
        this.contents = contents;
    }
}
