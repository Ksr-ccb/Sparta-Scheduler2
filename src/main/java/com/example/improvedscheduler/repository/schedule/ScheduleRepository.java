package com.example.improvedscheduler.repository.schedule;

import com.example.improvedscheduler.entity.schedule.Schedule;
import com.example.improvedscheduler.common.exception.ResourceNotFoundException;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    default Schedule findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new ResourceNotFoundException("해당 아이디 값이 존재하지 않습니다. : " + id));
    }

    List<Schedule> findAllByUser_Id(Long id);

    void deleteByUser_Id(Long userId);
}
