package com.example.idus_exam.member.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Builder
public class EmailVerify {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "인증 정보 ID", example = "1")
    private Long idx;

    @Schema(description = "이메일 인증에 사용되는 UUID", example = "d70a0b84-bc9c-4b6b-a5f7-272bcfe05bdb")
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "member_idx")
    @Schema(description = "이메일 인증과 관련된 회원", example = "John Doe")
    private Member member;
}
