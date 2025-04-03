package com.example.improvedscheduler.service.schedule;

import com.example.improvedscheduler.common.dto.comment.CommentResponseDto;
import com.example.improvedscheduler.common.dto.schedule.MultipleSchedulesResponseDto;
import com.example.improvedscheduler.common.dto.schedule.ScheduleResponseDto;
import com.example.improvedscheduler.entity.schedule.Schedule;
import com.example.improvedscheduler.entity.user.User;
import com.example.improvedscheduler.common.exception.UnauthorizedActionException;
import com.example.improvedscheduler.repository.schedule.ScheduleRepository;
import com.example.improvedscheduler.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    /**
     * 새로운 일정을 저장하는 메서드 입니다.
     * 제목과 내용을 받아와서 저장합니다.
     * @param title 일정 제목
     * @param contents 일정 내용
     * @param id 일정 생성자의 사용자 ID
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
     *
     * @param pageNum  페이지 번호
     * @param pageSize 페이지 크기
     * @return 일정 목록을 담은 페이지 DTO
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MultipleSchedulesResponseDto> getAllSchedulesPaged(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC,
                "updateDate"));

        return scheduleRepository.findAll(pageable)
                .map(MultipleSchedulesResponseDto::toMultipleResponseDto);
    }

    /**
     * 전체 일정 목록을 조회하는 메서드입니다.
     * @return 일정 목록을 담은 DTO 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public List<MultipleSchedulesResponseDto> findAll() {
        return scheduleRepository.findAll()
                .stream()
                .map(MultipleSchedulesResponseDto::toMultipleResponseDto)
                .toList();
    }

    /**
     * 특정 사용자의 일정 목록을 조회하는 메서드입니다.
     * 로그인 상태에서 해당 회원이 작성한 일정들을 리스트로 불러오고 반환합니다.
     * @param userId 사용자 ID
     * @return 해당 사용자의 일정 목록을 담은 DTO 리스트
     */
    @Override
    public List<MultipleSchedulesResponseDto> findMySchedules(Long userId) {
        List<Schedule> schedules = scheduleRepository.findAllByUser_Id(userId);
        return schedules
                .stream()
                .map(MultipleSchedulesResponseDto::toMultipleResponseDto)
                .toList();
    }

    /**
     * 일정 정보를 수정하는 메서드입니다.
     * 사용자가 수정하고자 하는 일정이 존재해야 하며, 제목과 내용을 선택적으로 수정 가능합니다.
     *
     * @param scheduleId  일정 ID
     * @param loginUserId 로그인한 유저 아이디
     * @param title 변경할 제목 (null 가능)
     * @param contents 변경할 내용 (null 가능)
     * @return 수정된 일정 정보를 담은 DTO
     * @throws ResponseStatusException 일정이 존재하지 않을 경우 예외 발생
     */
    @Override
    public ScheduleResponseDto updateSchedule(Long scheduleId, Long loginUserId, String title, String contents) {

        checkWriter(scheduleId, loginUserId);

        Schedule schedule = scheduleRepository.findByIdOrElseThrow(scheduleId);

        if( title != null){
            schedule.setTitle(title);
        }

        if( contents != null){
            schedule.setContents(contents);
        }

        scheduleRepository.save(schedule);
        return toScheduleResponseDto(schedule);
    }

    /**
     * 일정 삭제 메서드입니다.
     * 삭제하고자 하는 일정이 존재해야합니다.
     *
     * @param scheduleId 삭제할 일정 ID
     * @param loginUserId 로그인 한 유저 아이디
     * @throws ResponseStatusException 일정이 존재하지 않을 경우 예외 발생
     */
    @Override
    public void deleteSchedule(Long scheduleId, Long loginUserId) {

        checkWriter(scheduleId, loginUserId);

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
    private void checkWriter(Long scheduleId, Long loginUserId) {
        Schedule schedule = scheduleRepository.findByIdOrElseThrow(scheduleId);
        Long correspondingId = schedule.getUser().getId();
        if(!correspondingId.equals(loginUserId)){
            throw new UnauthorizedActionException("작성한 본인만 삭제할 수 있습니다.");
        }
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
     * Schedule 엔티티를 ScheduleResponseDto로 변환하는 메서드입니다.
     * 여러 메서드에서 반복적으로 사용되고 있어서 유틸을위해 작성되엇습니다.
     * @param schedule 변환할 일정 엔티티
     * @return 일정 정보를 담은 DTO
     */
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
