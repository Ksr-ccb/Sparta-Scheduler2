package com.example.improvedscheduler.common.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateCommentRequestDto {
    @NotBlank(message = "내용 입력은 필수입니다.")
    private final String contents;

    public CreateCommentRequestDto(String contents) {
        this.contents = contents;
    }
}
