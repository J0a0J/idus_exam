package com.example.idus_exam.member;

import com.example.idus_exam.member.model.Member;
import com.example.idus_exam.member.model.MemberDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 가입", description = "회원 가입하는 기능입니다.")
    @PostMapping("/signup")
    public ResponseEntity<MemberDto.SignupResponse> signup(@RequestBody MemberDto.SignupRequest dto) {
        MemberDto.SignupResponse response = memberService.signup(dto);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원 인증", description = "이메일 인증을 하는 기능입니다.")
    @GetMapping("/verify")
    public void verify(String uuid) {
        memberService.verify(uuid);
    }

    @Operation(summary = "로그인", description = "로그인 하는 기능입니다.")
    @PostMapping("/login")
    public void login(Member member) {
    }
}
