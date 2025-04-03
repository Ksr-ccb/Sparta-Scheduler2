package com.example.improvedscheduler.controller.schedule;

import com.example.improvedscheduler.dto.schedule.*;
import com.example.improvedscheduler.dto.user.UserResponseDto;
import com.example.improvedscheduler.exception.WrongApproachException;
import com.example.improvedscheduler.service.schedule.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * ScheduleController 는 url/schedules 으로 들어오는 요청을 처리합니다.
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
     * 데이터 모두를 불러옵니다.
     * 불러올때 페이지의 크기와 순서를 지정할 수 잇습니다.
     * 만약에 불러올 데이터가 없다면 빈 페이지를 반환합니다.
     * 만약 아무 조건없이 실행한다면 pageNum = 0 , pageSize = 10이 됩니다.
     */
    @GetMapping("/pages")
    public ResponseEntity<Page<MultipleSchedulesResponseDto>> findAllSchedulePaged(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ){

        return new ResponseEntity<>(scheduleService.getAllSchedulesPaged(pageNum-1,pageSize), HttpStatus.OK);
    }


    /**
     * 전체 스케줄 목록을 조회하는 API 입니다.
     * @return 전체 스케줄 목록을 담은 List<ScheduleResponseDto>와 HTTP 상태 코드 200(OK)
     * 로그인을 하지 않더라도 이용할 수 있습니다.
     */
    @GetMapping
    public ResponseEntity<List<MultipleSchedulesResponseDto>> findAll() {

        return new ResponseEntity<>(scheduleService.findAll(), HttpStatus.OK);
    }

    /**
     * 현재 로그인한 사용자의 스케줄 목록을 조회하는 API 입니다.
     * @param request 현재 사용자의 HTTP 요청 객체
     * @return 로그인한 사용자의 스케줄 목록을 담은 List<ScheduleResponseDto>와 HTTP 상태 코드 200(OK)
     * 필터에서 로그인 유무를 확인하고옵니다.
     */
    @GetMapping("/myschedules")
    public ResponseEntity<List<MultipleSchedulesResponseDto>> findMySchedules(
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        UserResponseDto loginUser = (UserResponseDto) session.getAttribute("loginUser");

        List<MultipleSchedulesResponseDto> scheduleResponseDtoList = scheduleService.findMySchedules(loginUser.getId());

        return new ResponseEntity<>(scheduleResponseDtoList, HttpStatus.OK);
    }


    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(
            @PathVariable Long scheduleId
    ) {

        return new ResponseEntity<>(scheduleService.findScheduleById(scheduleId), HttpStatus.OK);
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

        ScheduleResponseDto scheduleResponseDto = scheduleService.updateSchedule(scheduleId, loginUser.getId(),
                updateScheduleDto.getTitle(), updateScheduleDto.getContents());
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
        
        scheduleService.deleteSchedule(scheduleId, loginUser.getId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /***************밑은 덧글 api******************/

    /**
     * 특정 일정에 댓글을 추가하는 API 입니다.
     * 요청 본문에서 댓글 내용을 받아와 로그인한 사용자 정보를 기반으로 댓글을 생성합니다.
     * @param dto 댓글 생성 요청 DTO
     * @param scheduleId 댓글을 추가할 일정 ID
     * @param request HTTP 요청 객체 (세션에서 로그인 사용자 정보 가져오기 위함)
     * @return 댓글이 추가된 일정 응답 DTO
     * @throws ResponseStatusException 일정이 존재하지 않은 경우
     */
    @PostMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> createComment(
            @RequestBody CreateCommentRequestDto dto,
            @PathVariable Long scheduleId,
            HttpServletRequest request){

        HttpSession session = request.getSession(false);
        UserResponseDto loginUser = (UserResponseDto) session.getAttribute("loginUser");

        return new ResponseEntity<>(scheduleService.createComment(scheduleId,loginUser.getId(),dto.getContents()), HttpStatus.CREATED);
    }

    @PutMapping("/{scheduleId}/{commentId}")
    public ResponseEntity<ScheduleResponseDto> updateComment(
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            HttpServletRequest request,
            @RequestBody UpdateCommentRequestDto dto
        ){
        HttpSession session = request.getSession(false);
        UserResponseDto loginUser = (UserResponseDto) session.getAttribute("loginUser");


        return new ResponseEntity<>(scheduleService.updateComment(scheduleId,commentId,loginUser.getId(), dto.getContents()),HttpStatus.OK);
    }


    @DeleteMapping("/{scheduleId}/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            HttpServletRequest request
    ){
        HttpSession session = request.getSession(false);
        UserResponseDto loginUser = (UserResponseDto) session.getAttribute("loginUser");

        scheduleService.deleteComment(scheduleId,commentId,loginUser.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
