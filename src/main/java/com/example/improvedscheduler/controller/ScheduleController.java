package com.example.improvedscheduler.controller;

import com.example.improvedscheduler.dto.UserResponseDto;
import com.example.improvedscheduler.dto.scheduleRequest.CreateScheduleDto;
import com.example.improvedscheduler.dto.ScheduleResponseDto;
import com.example.improvedscheduler.service.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param dto
     * @return
     */
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(
            @RequestBody CreateScheduleDto dto,
            HttpServletRequest request
    ){

        HttpSession session = request.getSession(false);

        // session에 저장된 유저정보 조회
        UserResponseDto loginUser = (UserResponseDto) session.getAttribute("loginUser");


        return new ResponseEntity<>(scheduleService.saveSchedule(dto.getTitle(), dto.getContents(),
                loginUser.getId()), HttpStatus.CREATED);
    }

}
