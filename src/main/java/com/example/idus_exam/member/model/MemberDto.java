package com.example.idus_exam.member.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberDto {

    @Getter
    public static class SignupRequest {

        @Schema(description = "사용자의 이름", example = "John Doe")
        private String name;

        @Schema(description = "사용자의 닉네임", example = "johnny123")
        private String nickname;

        @Schema(description = "사용자의 비밀번호", example = "password123")
        private String password;

        @Schema(description = "사용자의 전화번호", example = "010-1234-5678")
        private String phone;

        @Schema(description = "사용자의 이메일", example = "john.doe@example.com")
        private String email;

        @Schema(description = "사용자의 성별", example = "M", allowableValues = {"M", "F"})
        private String gender;

        public Member toEntity(String password) {
            Member member = Member.builder()
                    .name(name)
                    .nickname(nickname)
                    .password(password)
                    .phone(phone)
                    .email(email)
                    .gender(gender)
                    .build();

            return member;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignupResponse {

        @Schema(description = "회원 ID", example = "1")
        private Long idx;

        @Schema(description = "회원 이메일", example = "john.doe@example.com")
        private String email;

        @Schema(description = "회원 닉네임", example = "johnny123")
        private String nickname;

        public static SignupResponse from(Member entity) {
            return new SignupResponse(entity.getIdx(), entity.getEmail(), entity.getNickname());
        }
    }
}
