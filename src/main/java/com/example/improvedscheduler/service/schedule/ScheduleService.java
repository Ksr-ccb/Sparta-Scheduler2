package com.example.improvedscheduler.service.schedule;

import com.example.improvedscheduler.common.dto.schedule.MultipleSchedulesResponseDto;
import com.example.improvedscheduler.common.dto.schedule.ScheduleResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ScheduleService {
    ScheduleResponseDto saveSchedule(String title, String contents, Long id);

    List<MultipleSchedulesResponseDto> getAllSchedulesPaged(int pageNum, int pageSize);

    List<MultipleSchedulesResponseDto> findAll();

    List<MultipleSchedulesResponseDto> findMySchedules(Long id);

    ScheduleResponseDto updateSchedule(Long id, Long loginUserId, String title, String contents);

    void deleteSchedule(Long scheduleId, Long id);

    ScheduleResponseDto findScheduleById(Long scheduleId);

}
