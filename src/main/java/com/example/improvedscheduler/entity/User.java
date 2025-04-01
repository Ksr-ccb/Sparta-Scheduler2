package com.example.improvedscheduler.entity;

import jakarta.persistence.*;
import lombok.Getter;

/**
 * JPA 사용을 위해 엔티티 등록을 해줍니다.
 * 사용하는 테이블 이름은 USER입니다.
 */
@Getter
@Entity
@Table(name = "user")
public class User extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    public User(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User() {

    }
}
