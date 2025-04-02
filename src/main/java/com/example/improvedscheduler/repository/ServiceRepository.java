package com.example.improvedscheduler.repository;

import com.example.improvedscheduler.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository  extends JpaRepository<Schedule, Long> {
}
