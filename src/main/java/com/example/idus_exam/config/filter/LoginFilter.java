package com.example.idus_exam.config.filter;


import com.example.idus_exam.member.model.Member;
import com.example.idus_exam.member.model.MemberDto;
import com.example.idus_exam.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    // 원래는 form-data 형식으로 사용자 정보를 입력받았는데
    // 우리는 JSON 형태로 입력을 받기 위해서 재정의
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("LoginFilter 실행됐다.");
        UsernamePasswordAuthenticationToken authToken;
        // 그림에서 1번 로직
//        MemberDto.SignupRequest MemberDto =
//                new MemberDto.SignupRequest(request.getParameter("Membername"), request.getParameter("password"));
        try {
            // 그림에서 원래 1번이었던 로직을 JSON 형태의 데이터를 처리하도록 변경
            MemberDto.SignupRequest MemberDto  = new ObjectMapper().readValue(request.getInputStream(), MemberDto.SignupRequest.class);

            // 그림에서 2번 로직
            authToken =
                    new UsernamePasswordAuthenticationToken(MemberDto.getEmail(), MemberDto.getPassword(), null);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 그림에서 3번 로직
        return authenticationManager.authenticate(authToken);
    }

    
    // 그림에서 9번 로직
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Member Member = (Member) authResult.getPrincipal();
        String jwtToken = JwtUtil.generateToken(Member.getIdx(), Member.getEmail(), Member.getNickname());

        
//        일반적인 객체 생성 및 객체의 변수에 값을 설정하는 방법
//        ResponseCookie cookie = new ResponseCookie();
//        cookie.setPath("/");
//        cookie.setHttpOnly(true);

//        빌더 패턴으로 객체를 생성하면서 값을 설정하는 방법
        ResponseCookie cookie = ResponseCookie
                .from("ATOKEN", jwtToken)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(Duration.ofHours(1L))
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());



    }
}
