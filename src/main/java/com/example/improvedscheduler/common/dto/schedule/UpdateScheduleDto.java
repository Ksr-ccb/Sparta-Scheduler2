package com.example.improvedscheduler.common.dto.schedule;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateScheduleDto {

    @Size(max = 10, message = "제목은 10글자 이내로 작성가능합니다.")
    private final String title;


    private final String contents;

    public UpdateScheduleDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
