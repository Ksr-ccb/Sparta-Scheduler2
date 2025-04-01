package com.example.improvedscheduler.service;

import com.example.improvedscheduler.dto.UserResponseDto;
import com.example.improvedscheduler.entity.User;
import com.example.improvedscheduler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * UserServiceImpl는  {@link UserService} 인터페이스를 구현합니다.
 * UserServiceImpl에서는 회원가입, 로그인, 유저 정보 불러오기, 유저 정보 수정하기, 회원탈퇴 기능을 수행합니다.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    /**
     * 회원 가입 메서드입니다.
     * 사용자의 정보를 받아 새로운 계정을 생성하고 저장한 후, 응답 DTO로 변환하여 반환합니다.
     * @param username 가입할 사용자의 아이디
     * @param password 가입할 사용자의 비밀번호
     * @param email 가입할 사용자의 이메일
     * @return 생성된 사용자의 정보를 담은 UserResponseDto 객체
     */
    public UserResponseDto signUp(String username, String password, String email) {
        User user = new User(username,password,email);
        User savedUser = userRepository.save(user);
        return new UserResponseDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    /**
     * 사용자의 로그인 요청을 처리하는 메서드입니다.
     * 입력된 이메일과 비밀번호를 기반으로 사용자를 조회하고, 인증이 성공하면 응답 DTO로 반환합니다..
     * @param email 로그인할 사용자의 이메일
     * @param password 로그인할 사용자의 비밀번호
     * @return 로그인된 사용자의 정보를 담은 UserResponseDto 객체
     * @throws ResponseStatusException 이메일 또는 비밀번호가 일치하지 않을 경우 401 UNAUTHORIZED 예외 발생
     */
    public UserResponseDto login(String email, String password) {
        Optional<User> loginUser = userRepository.findByEmailAndPassword(email, password);

        if( loginUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일과 비밀번호를 확인해주세요");
        }

        User user = userRepository.findByIdOrElseThrow(loginUser.get().getId());

        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail());
    }
}

