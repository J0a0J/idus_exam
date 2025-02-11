package com.example.idus_exam;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Operation(summary = "홈 화면", description = "로그아웃 후 리디렉션될 홈 화면입니다.")
    @GetMapping("/")
    public String home() {
        return "logout successful";
    }
}
