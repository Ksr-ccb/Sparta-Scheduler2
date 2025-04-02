package com.example.improvedscheduler.service;

import com.example.improvedscheduler.dto.ScheduleResponseDto;
import com.example.improvedscheduler.dto.UserResponseDto;
import com.example.improvedscheduler.dto.scheduleRequest.CreateScheduleDto;
import com.example.improvedscheduler.entity.Schedule;
import com.example.improvedscheduler.entity.User;
import com.example.improvedscheduler.repository.ServiceRepository;
import com.example.improvedscheduler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{

    private final ServiceRepository serviceRepository;
    private final UserRepository userRepository;

    @Override
    public ScheduleResponseDto saveSchedule(String title, String contents, Long id) {

        User user = userRepository.findByIdOrElseThrow(id);

        Schedule schedule = new Schedule(title,contents);
        schedule.setUesr(user);

        serviceRepository.save(schedule);

        return new ScheduleResponseDto(schedule.getId(), schedule.getTitle(), schedule.getContents(),
                schedule.getCreateDate(), schedule.getUpdateDate());

    }


}
