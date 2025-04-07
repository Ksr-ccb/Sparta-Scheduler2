package com.example.improvedscheduler.service.user;

import static org.junit.jupiter.api.Assertions.*;

import com.example.improvedscheduler.common.dto.user.UserResponseDto;
import com.example.improvedscheduler.common.exception.ResourceNotFoundException;
import com.example.improvedscheduler.repository.user.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.improvedscheduler.entity.user.User;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;


    String username;
    String password;

    @BeforeEach
    void setUp() {
        username = "테스트이름";
        password = "password123";
    }


    @Test
    @DisplayName("회원가입 성공")
    void signUpSuccess() {
        // given
        String email = "test1@email.com";

        given(userRepository.findByEmail(email)).willReturn(Optional.empty());

        User user = new User(username, password, email);
        user.setId(1L); // 테스트용 ID 지정
        given(userRepository.save(any(User.class))).willReturn(user);

        // when
        UserResponseDto result = userService.signUp(username, password, email);

        // then
        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void signUpDuplicateEmailFail() {
        // given
        String email = "test1@email.com";

        User existingUser = new User(username, password, email);
        given(userRepository.findByEmail(email)).willReturn(Optional.of(existingUser));

        // when & then
        assertThatThrownBy(() -> userService.signUp(username, password, email))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("회원가입이 불가능한 이메일입니다.");
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 형식 아님")
    void signUpInvalidEmailFail() {
        // given
        String email = "djiahfibcvhuaweb";

        // when & then
        assertThatThrownBy(() -> userService.signUp(username, password, email))
            .isInstanceOf(MethodArgumentNotValidException.class)
            .hasMessage("올바르지 않은 이메일 형식입니다.");
    }
}