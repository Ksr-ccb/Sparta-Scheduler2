package com.example.improvedscheduler.repository;

import com.example.improvedscheduler.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    default Schedule findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id));
    }

    Page<Schedule> findAllByOrderByIdAsc(Pageable pageable);

    List<Schedule> findAllByUser_Id(Long id);

    void deleteByUser_Id(Long userId);
}
