package com.example.improvedscheduler.service;

import com.example.improvedscheduler.dto.ScheduleResponseDto;
import com.example.improvedscheduler.dto.scheduleRequest.CreateScheduleDto;

public interface ScheduleService {
    ScheduleResponseDto saveSchedule(String title, String contents, Long id);

}
