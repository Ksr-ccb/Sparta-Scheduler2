package com.example.improvedscheduler.controller;

import com.example.improvedscheduler.dto.user.UserResponseDto;
import com.example.improvedscheduler.dto.schedule.CreateScheduleDto;
import com.example.improvedscheduler.dto.schedule.ScheduleResponseDto;
import com.example.improvedscheduler.dto.schedule.UpdateScheduleDto;
import com.example.improvedscheduler.service.ScheduleService;
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
     * 새로운 일정을 만들기 위한 함수르 불러내는 컨트롤러입니다.
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인이 필요합니다.");
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
     * 전체 스케줄 목록을 조회하는 API입니다.
     * @return 전체 스케줄 목록을 담은 List<ScheduleResponseDto>와 HTTP 상태 코드 200(OK)
     * 로그인을 하지 않더라도 이용할 수 있습니다.
     */
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findAll() {

        List<ScheduleResponseDto> scheduleResponseDtoList = scheduleService.findAll();

        return new ResponseEntity<>(scheduleResponseDtoList, HttpStatus.OK);
    }

    /**
     * 현재 로그인한 사용자의 스케줄 목록을 조회하는 API입니다.
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


    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long scheduleId,
            @RequestBody UpdateScheduleDto updateScheduleDto
            ){

        ScheduleResponseDto scheduleResponseDto = scheduleService.updateSchedule(scheduleId, updateScheduleDto.getTitle(), updateScheduleDto.getContents());
        return new ResponseEntity<>(scheduleResponseDto, HttpStatus.OK);
    }

    /**
     * 아이디 값에 맞는 스케줄 row의 내용을 삭제하는 함수입니다.
     * 삭제를 위해서 스케줄 row에 맞는 비밀번호를 입력받아야합니다.
     * @param scheduleId 삭제할 스케쥴의 아이디 값 입니다.
     * @return 삭제 후에 되돌아가는 응답 값이 없기 때문에 성공시 NO_CONTENT 를 반환합니다.
     */
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long scheduleId){

        scheduleService.deleteSchedule(scheduleId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
