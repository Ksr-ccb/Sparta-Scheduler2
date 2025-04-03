package com.example.improvedscheduler.repository.schedule;

import com.example.improvedscheduler.entity.schedule.Schedule;
import com.example.improvedscheduler.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    default Schedule findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new ResourceNotFoundException("해당 아이디 값이 존재하지 않습니다. : " + id));
    }

    Page<Schedule> findAllByOrderByIdAsc(Pageable pageable);

    List<Schedule> findAllByUser_Id(Long id);

    void deleteByUser_Id(Long userId);
}
