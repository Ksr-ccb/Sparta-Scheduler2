package com.example.improvedscheduler.service.schedule;

import com.example.improvedscheduler.dto.schedule.ScheduleResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ScheduleService {
    ScheduleResponseDto saveSchedule(String title, String contents, Long id);

    Page<ScheduleResponseDto> getAllSchedulesPaged(Long pageNum, Long pageSize);

    List<ScheduleResponseDto> findAll();

    List<ScheduleResponseDto> findMySchedules(Long id);

    ScheduleResponseDto updateSchedule(Long id, String title, String contents);

    void deleteSchedule(Long scheduleId);

    Long getUserIdByScheduleId(Long scheduleId);

    ScheduleResponseDto createComment(Long scheduleId, Long id, String contents);

    ScheduleResponseDto findScheduleById(Long scheduleId);
}
