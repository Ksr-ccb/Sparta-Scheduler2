package com.example.improvedscheduler.service.schedule;

import com.example.improvedscheduler.dto.schedule.CommentResponseDto;
import com.example.improvedscheduler.dto.schedule.ScheduleResponseDto;
import com.example.improvedscheduler.entity.schedule.Comment;
import com.example.improvedscheduler.entity.schedule.Schedule;
import com.example.improvedscheduler.entity.user.User;
import com.example.improvedscheduler.repository.schedule.CommentRepository;
import com.example.improvedscheduler.repository.schedule.ScheduleRepository;
import com.example.improvedscheduler.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public ScheduleResponseDto saveSchedule(String title, String contents, Long id) {

        User user = userRepository.findByIdOrElseThrow(id);

        Schedule schedule = new Schedule(title,contents);
        schedule.setUser(user);

        scheduleRepository.save(schedule);

        return toScheduleResponseDto(schedule);

    }

    @Override
    public Page<ScheduleResponseDto> getAllSchedulesPaged(Long pageNum, Long pageSize) {
        Pageable pageable = PageRequest.of(pageNum.intValue(), pageSize.intValue());

        Page<Schedule> schedulePage = scheduleRepository.findAllByOrderByIdAsc(pageable);
        return schedulePage.map(this::toScheduleResponseDto);
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

    @Override
    public ScheduleResponseDto updateSchedule(Long scheduleId, String title, String contents) {
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(scheduleId);

        if( title != null){
            schedule.setTitle(title);
        }

        if( contents != null){
            schedule.setContents(contents);
        }

        return toScheduleResponseDto(schedule);
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(scheduleId);
        return toScheduleResponseDto(schedule);
    }

    @Override
    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(scheduleId);
        scheduleRepository.delete(schedule);
    }

    @Override
    public Long getUserIdByScheduleId(Long scheduleId) {
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(scheduleId);
        return schedule.getUser().getId();
    }

    @Override
    public ScheduleResponseDto createComment(Long scheduleId, Long id, String contents) {
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(scheduleId);
        User user = userRepository.findByIdOrElseThrow(id);

        Comment comment = new Comment(contents);
        comment.setSchedule(schedule);
        comment.setUser(user);

        commentRepository.save(comment);

        schedule.addComment(comment);
        return toScheduleResponseDto(schedule);
    }



    private ScheduleResponseDto toScheduleResponseDto(Schedule schedule){
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
