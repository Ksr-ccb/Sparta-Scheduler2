package com.example.improvedscheduler.common.dto.schedule;

import com.example.improvedscheduler.common.dto.comment.CommentResponseDto;
import com.example.improvedscheduler.entity.schedule.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ScheduleResponseDto {

    public final Long id;

    public final String title;

    public final String contents;

    public final String name;

    public final LocalDateTime createDate;

    public final LocalDateTime updateDate;

    public final List<CommentResponseDto> comments;

    public ScheduleResponseDto(Long id, String title, String contents, String name, LocalDateTime createDate, LocalDateTime updateDate, List<CommentResponseDto> comments) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.name = name;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.comments = comments;
    }

    public static ScheduleResponseDto toResponseDto(Schedule schedule) {
        return new ScheduleResponseDto(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContents(),
                schedule.getUser().getUsername(),
                schedule.getCreateDate(),
                schedule.getUpdateDate(),
                schedule.getComments().stream()
                        .map(CommentResponseDto::toResponseDto)
                        .toList());
    }
}
