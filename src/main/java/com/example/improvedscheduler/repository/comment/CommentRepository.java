package com.example.improvedscheduler.repository.comment;

import com.example.improvedscheduler.entity.comment.Comment;
import com.example.improvedscheduler.common.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    default Comment findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new ResourceNotFoundException("해당 아이디 값이 존재하지 않습니다. : " + id));
    }
}
