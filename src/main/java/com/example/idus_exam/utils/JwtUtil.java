package com.example.idus_exam.utils;

import com.example.idus_exam.member.model.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    private static String SECRET;
    @Value("${jwt.expired}")
    private int exp;
    private static int EXP;


    @PostConstruct
    public void init() {
        SECRET = secret;
        EXP = exp;
    }

    public static Member getMember(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Member.builder()
                    .idx(claims.get("userIdx", Long.class))
                    .email(claims.get("userEmail", String.class))
                    .nickname(claims.get("userEmail", String.class))
                    .build();

        } catch (ExpiredJwtException e) {
            System.out.println("토큰이 만료되었습니다!");
            return null;
        }
    }

    public static String generateToken(Long userIdx, String userEmail, String userNickName) {
        Claims claims = Jwts.claims();
        claims.put("userEmail", userEmail);
        claims.put("userNickName", userNickName);
        claims.put("userIdx", userIdx);
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXP))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
        return token;
    }

    public static boolean validate(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            System.out.println("토큰이 만료되었습니다!");
            return false;
        }
        return true;
    }
}
