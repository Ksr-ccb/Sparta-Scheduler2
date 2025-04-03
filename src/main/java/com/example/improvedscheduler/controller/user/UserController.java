package com.example.improvedscheduler.controller.user;

import com.example.improvedscheduler.common.dto.user.LogInRequestDto;
import com.example.improvedscheduler.common.dto.user.SignUpRequestDto;
import com.example.improvedscheduler.common.dto.user.UpdateRequestDto;
import com.example.improvedscheduler.common.dto.user.UserResponseDto;
import com.example.improvedscheduler.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * 새로운 유저를 만들기 위한 API 입니다.
     * POST METHOD를 통해서 RequestBody 의 Json 내용을 SignUpRequestDto 으로 변환하여 가져옵니다.
     * @param requestDto 회원가입 위해 필요한 변수는 '이름, 비밀번호, 이메일' 세가지 입니다.
     * @return 가입 정상 완료시 SignUpResponseDto 반환.(이름, 이메일, 회원번호)
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(
            @Valid @RequestBody SignUpRequestDto requestDto
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
     * 로그인 하는 API 입니다.
     * 입력받은 이메일과 비밀번호가 모두 일치하는 데이터가 있는 경우
     * 세션에 해당 유저의 정보를 저장합니다(아이디, 이름, 이메일)
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(
            @Valid @RequestBody LogInRequestDto requestDto,
            HttpServletRequest request
    ){
        UserResponseDto responseDto = userService.login(requestDto.getEmail(), requestDto.getPassword());

        HttpSession session = request.getSession(true);
        session.setAttribute("loginUser",responseDto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 현재 로그인한 사용자의 정보를 조회하는 API 입니다.
     * @param request 클라이언트의 HTTP 요청 객체로, 세션 정보를 포함합니다.
     * @return 로그인한 사용자의 정보를 담은 {@link UserResponseDto} 객체와 HTTP 상태 코드 200(OK)
     *         필터를통해 이미 로그인이 되어있는 것을 확인했기 때문에 바로 세션을 받아옵니다.
     */
    @GetMapping
    public ResponseEntity<UserResponseDto> getUserInfo(
            HttpServletRequest request
    ){
        HttpSession session = request.getSession(false);

        // session에 저장된 유저정보 조회
        UserResponseDto loginUser = (UserResponseDto) session.getAttribute("loginUser");
        return new ResponseEntity<>(loginUser, HttpStatus.OK);
    }

    /**
     * 사용자의 정보를 업데이트하는 API 입니다.
     * 사용자 이름, 비밀번호를 선택적으로 변경할 수 있습니다.
     * @param id 업데이트할 사용자 ID
     * @param requestDto 업데이트할 사용자 정보가 담긴 DTO 객체
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateInformation(
            @PathVariable Long id,
            @RequestBody UpdateRequestDto requestDto

    ) {
        userService.updatePassword(
                id,
                requestDto.getUsername(),
                requestDto.getOldPassword(), requestDto.getNewPassword());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 특정 사용자를 삭제하는 API 입니다.
     * 요청된 사용자 ID에 해당하는 사용자를 삭제합니다.
     * 사용자가 작성한 모든 스케줄도 함께 삭제됩니다.
     * @param userId 삭제할 사용자의 ID
     * @return HTTP 204 No Content 상태 코드 반환 (응답 본문 없음)
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 로그아웃을 하는 API 입니다.
     * @param request 현재 로그인 한 세션 값
     * @return HTTP 204 No Content 상태 코드 반환 (응답 본문 없음)
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request
    ) {

        HttpSession session = request.getSession(false);
        // 세션이 존재하면 -> 로그인이 된 경우
        if(session != null) {
            session.invalidate(); // 해당 세션(데이터)을 삭제한다.
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
