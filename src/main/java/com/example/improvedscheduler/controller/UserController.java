package com.example.improvedscheduler.controller;

import com.example.improvedscheduler.dto.LogInRequestDto;
import com.example.improvedscheduler.dto.SignUpRequestDto;
import com.example.improvedscheduler.dto.UserResponseDto;
import com.example.improvedscheduler.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * UserController url/users 으로 들어오는 요청을 처리합니다.
 * 회원가입, 회원정보 불러오기, 화원정보 수정, 회원정보 삭제 기능을 제공합니다.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 새로운 유저를 만들기 위한 함수입니다.
     * POST METHOD를 통해서 RequestBody 의 Json 내용을 SignUpRequestDto 으로 변환하여 가져옵니다.
     * @param requestDto 회원가입 위해 필요한 변수는 '이름, 비밀번호, 이메일' 세가지 입니다.
     * @return 가입 정상 완료시 SignUpResponseDto 반환.(이름, 이메일, 회원번호)
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(
            @RequestBody SignUpRequestDto requestDto
    ) {
        UserResponseDto signUpResponseDto =
                userService.signUp(
                        requestDto.getUsername(),
                        requestDto.getPassword(),
                        requestDto.getEmail()
                );

        return new ResponseEntity<>(signUpResponseDto, HttpStatus.CREATED);
    }

    /**
     * 로그인 하는 함수입니다.
     * 입력받은 이메일과 비밀번호가 모두 일치하는 데이터가 있는 경우
     * 세션에 해당 유저의 정보를 저장합니다(아이디, 이름, 이메일)
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(
            @RequestBody LogInRequestDto requestDto,
            HttpServletRequest request
    ){
        UserResponseDto responseDto = userService.login(requestDto.getEmail(), requestDto.getPassword());
        Long userId = responseDto.getId();

        // 실패시 예외처리
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일과 비밀번호를 확인해주세요");
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginUser",responseDto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
