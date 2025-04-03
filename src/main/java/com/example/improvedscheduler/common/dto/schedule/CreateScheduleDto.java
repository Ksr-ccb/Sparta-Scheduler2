package com.example.improvedscheduler.common.dto.schedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateScheduleDto {

    @Size(max = 10, message = "제목은 10글자 이내로 작성가능합니다.")
    @NotBlank(message = "제목 입력은 필수입니다.")
    private final String title;

    @NotBlank(message = "내용 입력은 필수입니다.")
    private final String contents;

    public CreateScheduleDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
