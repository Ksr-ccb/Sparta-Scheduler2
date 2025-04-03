package com.example.improvedscheduler.service.comment;

import com.example.improvedscheduler.common.dto.comment.CommentResponseDto;
import com.example.improvedscheduler.common.dto.schedule.ScheduleResponseDto;
import com.example.improvedscheduler.entity.comment.Comment;
import com.example.improvedscheduler.entity.schedule.Schedule;
import com.example.improvedscheduler.entity.user.User;
import com.example.improvedscheduler.common.exception.UnauthorizedActionException;
import com.example.improvedscheduler.common.exception.WrongApproachException;
import com.example.improvedscheduler.repository.comment.CommentRepository;
import com.example.improvedscheduler.repository.schedule.ScheduleRepository;
import com.example.improvedscheduler.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    /**
     * 일정에 댓글을 추가하는 메서드입니다.
     * @param scheduleId 일정 ID
     * @param id 댓글 작성자의 사용자 ID
     * @param contents 댓글 내용
     * @return 댓글이 추가된 일정 정보를 담은 DTO
     * @throws ResponseStatusException 일정 또는 사용자가 존재하지 않을 경우 예외 발생
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
    /**
     * 특정 일정의 특정 댓글을 수정하는 메서드.
     *
     * @param scheduleId 수정할 댓글이 속한 일정 ID
     * @param commentId  수정할 댓글 ID
     * @param id  요청한 사용자의 ID
     * @param contents   수정할 댓글 내용
     * @throws WrongApproachException 댓글 작성자가 아닐 경우 예외 발생
     */
    @Override
    public ScheduleResponseDto updateComment(Long scheduleId, Long commentId, Long id, String contents) {
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);

        if(!comment.getUser().getId().equals(id)){
            throw new UnauthorizedActionException("작성한 본인만 수정할 수 있습니다.");
        }
        comment.setContents(contents);
        commentRepository.save(comment);

        Schedule schedule = scheduleRepository.findByIdOrElseThrow(scheduleId);
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

    @Override
    public void deleteComment(Long scheduleId, Long commentId, Long id) {
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);

        if(!comment.getUser().getId().equals(id)){
            throw new UnauthorizedActionException("작성한 본인만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }


}
