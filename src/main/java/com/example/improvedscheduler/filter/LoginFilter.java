package com.example.improvedscheduler.filter;

import com.example.improvedscheduler.exception.WrongApproachException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
@Component
public class LoginFilter implements Filter {
    // 인증을 하지 않아도될 URL Path 배열
    private static final String[] WHITE_LIST = {"/", "/users/signup", "/users/login", "/schedules", "/schedules/pages/**"};

    /**
     * [Filter] 로그인 여부를 확인하는 필터입니다.
     * 특정 요청 URI가 화이트리스트에 포함되지 않은 경우, 세션을 검사하여 로그인된 사용자인지 확인합니다.
     * 로그인되지 않은 경우 예외를 발생시킵니다.
     * @param servletRequest  클라이언트의 요청 객체
     * @param servletResponse 서버의 응답 객체
     * @param filterChain 필터 체인 객체로, 다음 필터로 요청을 전달하는 역할을 합니다.
     * @throws IOException 요청 처리 중 입출력 예외가 발생할 경우
     * @throws ServletException 서블릿 관련 예외가 발생할 경우
     */
    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String requestURI = httpRequest.getRequestURI();

        if(!isWhiteList(requestURI)){ //로그인 검사를 해야하는 uri인 경우
            HttpSession session = httpRequest.getSession(false); // < 로그인안됐으면 null 반환

            if( session == null || session.getAttribute("loginUser")== null){
                httpResponse.setContentType("application/json");
                httpResponse.setCharacterEncoding("UTF-8");
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
                httpResponse.getWriter().write("{\"message\": \"로그인 해주세요.\"}");
                return;
            }
        }
        
        //다음으로 넘기기
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 로그인 여부를 확인하는 URL인지 체크하는 메서드 입니다.
     * @param requestURI whiteListURL에 포함되는지 확인 합니다.
     * @return 포함되면 true 반환
     *          포함되지 않으면 false 반환
     */
    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }

}
