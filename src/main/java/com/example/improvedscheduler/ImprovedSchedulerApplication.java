package com.example.improvedscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ImprovedSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImprovedSchedulerApplication.class, args);
    }

}
