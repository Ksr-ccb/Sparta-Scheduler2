package com.example.improvedscheduler.service;

import com.example.improvedscheduler.dto.ScheduleResponseDto;
import com.example.improvedscheduler.entity.Schedule;
import com.example.improvedscheduler.entity.User;
import com.example.improvedscheduler.repository.ScheduleRepository;
import com.example.improvedscheduler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Override
    public ScheduleResponseDto saveSchedule(String title, String contents, Long id) {

        User user = userRepository.findByIdOrElseThrow(id);

        Schedule schedule = new Schedule(title,contents);
        schedule.setUser(user);

        scheduleRepository.save(schedule);

        return new ScheduleResponseDto(schedule.getId(), schedule.getTitle(), schedule.getContents(),
                schedule.getUser().getUsername(), schedule.getCreateDate(), schedule.getUpdateDate());

    }

    @Override
    public Page<ScheduleResponseDto> getAllSchedulesPaged(Long pageNum, Long pageSize) {
        Pageable pageable = PageRequest.of(pageNum.intValue(), pageSize.intValue());

        Page<Schedule> schedulePage = scheduleRepository.findAllByOrderByIdAsc(pageable);
        return schedulePage.map(schedule -> new ScheduleResponseDto(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContents(),
                schedule.getUser().getUsername(),
                schedule.getCreateDate(),
                schedule.getUpdateDate()
        ));
    }

    @Override
    public List<ScheduleResponseDto> findAll() {
        return scheduleRepository.findAll()
                .stream()
                .map(ScheduleResponseDto::toResponseDto)
                .toList();
    }

    @Override
    public List<ScheduleResponseDto> findMySchedules(Long userId) {
        List<Schedule> schedules = scheduleRepository.findAllByUser_Id(userId);
        return schedules
                .stream()
                .map(ScheduleResponseDto::toResponseDto)
                .toList();
    }

}
