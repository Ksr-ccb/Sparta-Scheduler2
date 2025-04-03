package com.example.improvedscheduler.controller.comment;


import com.example.improvedscheduler.common.dto.comment.CreateCommentRequestDto;
import com.example.improvedscheduler.common.dto.schedule.ScheduleResponseDto;
import com.example.improvedscheduler.common.dto.comment.UpdateCommentRequestDto;
import com.example.improvedscheduler.common.dto.user.UserResponseDto;
import com.example.improvedscheduler.service.comment.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * CommentController 도  ScheduleController처럼 url/schedules 으로 들어오는 요청을 처리합니다.
 * 덧글 쓰기, 수정, 삭제가 이뤄지는 위치가 하나의 할 일 보기 안에서 진행되기 때문입니다.
 * 서비스는 모두 로그인을 해야 이용이 가능합니다.
 */
@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
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

        return new ResponseEntity<>(commentService.createComment(scheduleId,loginUser.getId(),dto.getContents()), HttpStatus.CREATED);
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


        return new ResponseEntity<>(commentService.updateComment(scheduleId,commentId,loginUser.getId(), dto.getContents()),HttpStatus.OK);
    }


    @DeleteMapping("/{scheduleId}/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            HttpServletRequest request
    ){
        HttpSession session = request.getSession(false);
        UserResponseDto loginUser = (UserResponseDto) session.getAttribute("loginUser");

        commentService.deleteComment(scheduleId,commentId,loginUser.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
