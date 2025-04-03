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
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;


    /**
     * 새로운 일정을 저장하는 메서드 입니다.
     * 제목과 내용을 받아와서 저장합니다.
     * @param title    일정 제목
     * @param contents 일정 내용
     * @param id       일정 생성자의 사용자 ID
     * @return 저장된 일정 정보를 담은 DTO
     * @throws ResponseStatusException 사용자가 존재하지 않을 경우 예외 발생
     */
    @Override
    public ScheduleResponseDto saveSchedule(String title, String contents, Long id) {

        User user = userRepository.findByIdOrElseThrow(id);

        Schedule schedule = new Schedule(title,contents);
        schedule.setUser(user);

        scheduleRepository.save(schedule);

        return toScheduleResponseDto(schedule);

    }

    /**
     * 페이징 처리된 일정 목록을 조회하는 메서드입니다.
     * 페이지 번호와 크기를 조절할 수 있습니다.
     * @param pageNum  페이지 번호
     * @param pageSize 페이지 크기
     * @return 일정 목록을 담은 페이지 DTO
     */
    @Override
    public Page<ScheduleResponseDto> getAllSchedulesPaged(Long pageNum, Long pageSize) {
        Pageable pageable = PageRequest.of(pageNum.intValue(), pageSize.intValue());

        Page<Schedule> schedulePage = scheduleRepository.findAllByOrderByIdAsc(pageable);
        return schedulePage.map(this::toScheduleResponseDto);
    }

    /**
     * 전체 일정 목록을 조회하는 메서드입니다.
     * @return 일정 목록을 담은 DTO 리스트
     */
    @Override
    public List<ScheduleResponseDto> findAll() {
        return scheduleRepository.findAll()
                .stream()
                .map(ScheduleResponseDto::toResponseDto)
                .toList();
    }

    /**
     * 특정 사용자의 일정 목록을 조회하는 메서드입니다.
     * 로그인 상태에서 해당 회원이 작성한 일정들을 리스트로 불러오고 반환합니다.
     * @param userId 사용자 ID
     * @return 해당 사용자의 일정 목록을 담은 DTO 리스트
     */
    @Override
    public List<ScheduleResponseDto> findMySchedules(Long userId) {
        List<Schedule> schedules = scheduleRepository.findAllByUser_Id(userId);
        return schedules
                .stream()
                .map(ScheduleResponseDto::toResponseDto)
                .toList();
    }

    /**
     * 일정 정보를 수정하는 메서드입니다.
     * 사용자가 수정하고자 하는 일정이 존재해야 하며, 제목과 내용을 선택적으로 수정 가능합니다.
     * @param scheduleId 일정 ID
     * @param title      변경할 제목 (null 가능)
     * @param contents   변경할 내용 (null 가능)
     * @return 수정된 일정 정보를 담은 DTO
     * @throws ResponseStatusException 일정이 존재하지 않을 경우 예외 발생
     */
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

    /**
     * 일정 ID로 일정 정보를 조회하는 메서드입니다.
     * 조회하고자 하는 일정이 존재해야 하며, 일정 하나를 반환합니다.
     * @param scheduleId 일정 ID
     * @return 일정 정보를 담은 DTO
     * @throws ResponseStatusException 일정이 존재하지 않을 경우 예외 발생
     */
    @Override
    public ScheduleResponseDto findScheduleById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(scheduleId);
        return toScheduleResponseDto(schedule);
    }

    /**
     * 일정 삭제 메서드입니다.
     * 삭제하고자 하는 일정이 존재해야합니다.
     * @param scheduleId 삭제할 일정 ID
     * @throws ResponseStatusException 일정이 존재하지 않을 경우 예외 발생
     */
    @Override
    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(scheduleId);
        scheduleRepository.delete(schedule);
    }

    /**
     * 특정 일정의 작성자 ID를 조회하는 메서드입니다.
     * 일정을 수정/삭제할 때 본인만 할 수 있는데, 수정/삭제시 본인이 맞는지 검사하는 메서드입니다.
     * @param scheduleId 일정 ID
     * @return 일정 작성자의 사용자 ID
     * @throws ResponseStatusException 일정이 존재하지 않을 경우 예외 발생
     */
    @Override
    public Long getUserIdByScheduleId(Long scheduleId) {
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(scheduleId);
        return schedule.getUser().getId();
    }

    /**
     *
     * @param scheduleId
     * @param id
     * @param contents
     * @return
     */
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
