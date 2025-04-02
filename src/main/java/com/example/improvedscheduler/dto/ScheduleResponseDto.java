package com.example.improvedscheduler.dto;

import com.example.improvedscheduler.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponseDto {

    public final Long id;

    public final String title;

    public final String contents;

    public final String name;

    public final LocalDateTime createDate;

    public final LocalDateTime updateDate;

    public ScheduleResponseDto(Long id, String title, String contents, String name, LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.name = name;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public static ScheduleResponseDto toResponseDto(Schedule schedule) {
        return new ScheduleResponseDto(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContents(),
                schedule.getUser().getUsername(),
                schedule.getCreateDate(),
                schedule.getUpdateDate());
    }
}
