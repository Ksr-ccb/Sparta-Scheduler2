package com.example.improvedscheduler.common.dto.schedule;


import com.example.improvedscheduler.entity.schedule.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MultipleSchedulesResponseDto {

    public final Long id;

    public final String title;

    public final String contents;

    public final String name;

    public final LocalDateTime createDate;

    public final LocalDateTime updateDate;

    public final Long commentCount;

    public MultipleSchedulesResponseDto(Long id, String title, String contents, String name, LocalDateTime createDate, LocalDateTime updateDate, Long commentCount) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.name = name;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.commentCount = commentCount;
    }

    public static MultipleSchedulesResponseDto toMultipleResponseDto(Schedule schedule) {
        return new MultipleSchedulesResponseDto(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContents(),
                schedule.getUser().getUsername(),
                schedule.getCreateDate(),
                schedule.getUpdateDate(),
                (long) schedule.getComments().size());
    }

}
