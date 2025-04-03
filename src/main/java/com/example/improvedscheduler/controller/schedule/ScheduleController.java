package com.example.improvedscheduler.controller.schedule;

import com.example.improvedscheduler.dto.user.UserResponseDto;
import com.example.improvedscheduler.dto.schedule.CreateScheduleDto;
import com.example.improvedscheduler.dto.schedule.ScheduleResponseDto;
import com.example.improvedscheduler.dto.schedule.UpdateScheduleDto;
import com.example.improvedscheduler.exception.WrongApproachException;
import com.example.improvedscheduler.service.schedule.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * ScheduleController는 url/schedules 으로 들어오는 요청을 처리합니다.
 * 게시글 쓰기, 읽기, 수정, 삭제가 가능합니다.
 * 읽기를 제외한 서비스는 모두 로그인을 해야 이용이 가능합니다.
 */
@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * 새로운 일정을 만들기 위한 엔드포인트 입니다.
     * POST METHOD를 통해서 RequestBody의 Json 내용을 ScheduleRequestDto으로 변환하여 가져옵니다.
     * 새로운 일정을 만들기 위해 필요한 변수는 '제목, 내용' 두가지 입니다.
     * 로그인을 해야 이용이 가능합니다.
     * 해당 엔드포인트는 필터를 거치지 않기 떄문에 컨트롤러에서 예외처리를 해줍니다.
     * @param dto 새로운 일정 정보를 포함한 {@link CreateScheduleDto} 객체
     * @param request 현재 사용자의 HTTP 요청 객체 (세션 확인을 위해 사용)
     * @return 생성된 일정 정보를 담은 {@link ScheduleResponseDto}와 HTTP 상태 코드 201(CREATED)
     * @throws ResponseStatusException 로그인 정보가 없는 경우 HTTP 상태 코드 400(BAD REQUEST) 예외 발생
     */
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(
            @RequestBody CreateScheduleDto dto,
            HttpServletRequest request
    ){

        HttpSession session = request.getSession(false);

        if(session == null){
            throw new WrongApproachException("로그인이 필요합니다.");
        }
        // session에 저장된 유저정보 조회
        UserResponseDto loginUser = (UserResponseDto) session.getAttribute("loginUser");


        return new ResponseEntity<>(scheduleService.saveSchedule(dto.getTitle(), dto.getContents(),
                loginUser.getId()), HttpStatus.CREATED);
    }

    /**
     * 특정 조건에 맞는 데이터 모두를 불러옵니다.
     * 조건에 대한 설정 여부는 선택적입니다. ( OO / OX / XO/ XX 가능)
     * 만약 아무 조건없이 실행한다면 모든 ROWS를 불러옵니다.
     * @return
     */
//    @GetMapping
//    public ResponseEntity<List<ScheduleResponseDto>> findAllSchedulePaged(
//            @RequestParam(defaultValue = "0") Long pageNum,
//            @RequestParam(required = false, defaultValue = "10") Long pageSize){
//
//        Page<ScheduleResponseDto> newProductPage = scheduleService.getAllSchedulesPaged(pageNum,pageSize);
//
//        return new ResponseEntity<>(scheduleService.finaAllSchedules(), HttpStatus.OK);
//    }


    /**
     * 전체 스케줄 목록을 조회하는 API 입니다.
     * @return 전체 스케줄 목록을 담은 List<ScheduleResponseDto>와 HTTP 상태 코드 200(OK)
     * 로그인을 하지 않더라도 이용할 수 있습니다.
     */
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findAll() {

        List<ScheduleResponseDto> scheduleResponseDtoList = scheduleService.findAll();

        return new ResponseEntity<>(scheduleResponseDtoList, HttpStatus.OK);
    }

    /**
     * 현재 로그인한 사용자의 스케줄 목록을 조회하는 API 입니다.
     * @param request 현재 사용자의 HTTP 요청 객체
     * @return 로그인한 사용자의 스케줄 목록을 담은 List<ScheduleResponseDto>와 HTTP 상태 코드 200(OK)
     * 필터에서 로그인 유무를 확인하고옵니다.
     */
    @GetMapping("/myschedules")
    public ResponseEntity<List<ScheduleResponseDto>> findMySchedules(
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        UserResponseDto loginUser = (UserResponseDto) session.getAttribute("loginUser");

        List<ScheduleResponseDto> scheduleResponseDtoList = scheduleService.findMySchedules(loginUser.getId());

        return new ResponseEntity<>(scheduleResponseDtoList, HttpStatus.OK);
    }

    /**
     * 특정 스케줄을 수정합니다.
     * 작성한 본인만 수정할 수 있으며, 로그인은 필수입니다.
     * @param scheduleId 수정할 스케줄의 ID
     * @param updateScheduleDto 수정할 제목과 내용이 포함된 DTO (선택적)
     * @param request 로그인 사용자 확인을 위한 요청 객체
     * @return 수정된 스케줄 정보
     * @throws ResponseStatusException 작성자가 아닐 경우 수정 불가
     */
    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long scheduleId,
            @RequestBody UpdateScheduleDto updateScheduleDto,
            HttpServletRequest request
            ){

        HttpSession session = request.getSession(false);
        UserResponseDto loginUser = (UserResponseDto) session.getAttribute("loginUser");

        Long correspondingId = scheduleService.getUserIdByScheduleId(scheduleId);

        if(!correspondingId.equals(loginUser.getId())){
            throw new WrongApproachException("작성한 본인만 수정할 수 있습니다.");
        }

        ScheduleResponseDto scheduleResponseDto = scheduleService.updateSchedule(scheduleId, updateScheduleDto.getTitle(), updateScheduleDto.getContents());
        return new ResponseEntity<>(scheduleResponseDto, HttpStatus.OK);
    }

    /**
     * 아이디 값에 맞는 스케줄 row의 내용을 삭제하는 API 입니다.
     * 작성한 본인만 삭제할 수 있으며, 로그인은 필수입니다.
     * @param scheduleId 삭제할 스케줄의 ID
     * @param request 클라이언트 요청 정보 (세션에서 로그인 사용자 정보 확인)
     * @return HTTP 204 No Content 상태 코드 반환 (응답 본문 없음)
     * @throws ResponseStatusException 작성자가 아닌 경우 삭제 불가
     */
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long scheduleId,
            HttpServletRequest request
    ){
        HttpSession session = request.getSession(false);
        UserResponseDto loginUser = (UserResponseDto) session.getAttribute("loginUser");


        Long correspondingId = scheduleService.getUserIdByScheduleId(scheduleId);

        if(!correspondingId.equals(loginUser.getId())){
            throw new WrongApproachException("작성한 본인만 삭제할 수 있습니다.");
        }
        
        scheduleService.deleteSchedule(scheduleId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
