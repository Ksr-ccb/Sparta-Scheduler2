package com.example.improvedscheduler.repository;

import com.example.improvedscheduler.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Page<Schedule> findAllByOrderByIdAsc(Pageable pageable);

    List<Schedule> findAllByUser_Id(Long id);
}
