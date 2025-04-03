package com.example.improvedscheduler.dto.schedule;

import com.example.improvedscheduler.entity.schedule.Comment;
import com.example.improvedscheduler.entity.schedule.Schedule;

import java.time.LocalDateTime;

public class CommentResponseDto {

    public final Long id;

    public final String contents;

    public final String name;

    public final LocalDateTime createDate;

    public final LocalDateTime updateDate;

    public CommentResponseDto(Long id, String contents, String name, LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.contents = contents;
        this.name = name;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public static CommentResponseDto toResponseDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getContents(),
                comment.getUser().getUsername(),
                comment.getCreateDate(),
                comment.getUpdateDate());
    }

}
