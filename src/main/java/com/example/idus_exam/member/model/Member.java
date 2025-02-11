package com.example.idus_exam.member.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "회원 ID", example = "1")
    private Long idx;

    @Schema(description = "회원 이름", example = "John Doe")
    private String name;

    @Schema(description = "회원 닉네임", example = "johnny")
    private String nickname;

    @Schema(description = "회원 비밀번호", example = "password123")
    private String password;

    @Schema(description = "회원 전화번호", example = "010-1234-5678")
    private String phone;

    @Schema(description = "회원 이메일", example = "johndoe@example.com")
    private String email;

    @Schema(description = "회원 활성화 상태", example = "true")
    private boolean enable;

    @Schema(description = "회원 성별", example = "M", allowableValues = {"M", "F"})
    private String gender;

    @OneToMany(mappedBy = "member")
    @Schema(description = "회원과 관련된 이메일 인증 리스트")
    private List<EmailVerify> emailVerifyList = new ArrayList<>();

    @Schema(description = "회원 인증 상태를 활성화로 변경", example = "true")
    public void memberVerify() {
        this.enable = true;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        if (enable) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // Assuming the user is enabled, assign ROLE_USER
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_GUEST")); // You can assign a different role for disabled users (if applicable)
        }

        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }

    @Override
    public String getUsername() {
        return "";
    }
}
