package com.example.improvedscheduler.repository.user;

import com.example.improvedscheduler.entity.user.User;
import com.example.improvedscheduler.common.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    default User findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new ResourceNotFoundException("해당 아이디 값이 존재하지 않습니다. : " + id));
    }

    Optional<User> findByEmail(String email);
}



