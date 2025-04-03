package com.example.improvedscheduler.service.comment;

import com.example.improvedscheduler.common.dto.schedule.ScheduleResponseDto;

public interface CommentService {
    ScheduleResponseDto updateComment(Long scheduleId, Long commentId, Long userId, String contents);

    void deleteComment(Long scheduleId, Long commentId, Long id);

    ScheduleResponseDto createComment(Long scheduleId, Long id, String contents);

}
