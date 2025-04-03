package com.example.improvedscheduler.repository.schedule;

import com.example.improvedscheduler.entity.schedule.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
