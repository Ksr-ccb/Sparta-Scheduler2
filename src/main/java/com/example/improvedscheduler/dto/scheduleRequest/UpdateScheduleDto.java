package com.example.improvedscheduler.dto.scheduleRequest;

import lombok.Getter;

@Getter
public class UpdateScheduleDto {

    private final String title;
    private final String contents;

    public UpdateScheduleDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
