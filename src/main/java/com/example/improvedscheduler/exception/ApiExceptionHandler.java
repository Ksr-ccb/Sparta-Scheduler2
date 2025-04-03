package com.example.improvedscheduler.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // @ControllerAdvice + @ResponseBody
public class ApiExceptionHandler {
    /**
     * [예외] 비밀번호 인증에 실패하거나 유효하지 않은 값을 직젋 throw 했을 때 작동
     * ResponseStatusException 처리 (400, 401, 404 등)
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatusException(
            ExceptionThrowDirectly ex, HttpServletRequest request) {

        Map<String, String> errorResponse = new HashMap<>(); //<필드, 메시지>
        errorResponse.put("timestamp", String.valueOf(LocalDateTime.now()));
        errorResponse.put("message", ex.getReason()); // 메시지 넣기
        errorResponse.put("status", String.valueOf(ex.getStatusCode().value()));
        errorResponse.put( "path", request.getRequestURI());
        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }

    /**
     * [예외] 유효성 검사 실패 처리 (400 Bad Request)
     * @Valid, @eamil, @size 등등 or Dto에 맞지 않게 변수를 넣었을 때 작동
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * 기타 예외 처리 (예상치 못한 서버 에러)

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGlobalException(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "서버 내부 오류가 발생했습니다.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
     */
}
