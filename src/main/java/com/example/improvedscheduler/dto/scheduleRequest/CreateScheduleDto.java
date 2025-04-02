package com.example.improvedscheduler.dto.scheduleRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateScheduleDto {

    @NotBlank(message = "제목 입력은 필수입니다.")
    private final String title;

    @NotBlank(message = "내용 입력은 필수입니다.")
    private final String contents;

    public CreateScheduleDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
