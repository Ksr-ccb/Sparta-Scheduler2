package com.example.improvedscheduler.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponseDto {

    public final Long id;

    public final String title;

    public final String contents;

    public final LocalDateTime createDate;

    public final LocalDateTime updateDate;

    public ScheduleResponseDto(Long id, String title, String contents, LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
}
