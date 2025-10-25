package com.example.login.infra.controller;

import com.example.login.domain.dto.*;
import com.example.login.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginDto loginDto) {
        LoginResponseDto responseDto = loginService.userLogin(loginDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/reissue")
    public ResponseEntity<ReissueResponseDto> reissue(@RequestBody @Valid ReissueDto reissueDto){
        ReissueResponseDto responseDto = loginService.userReissue(reissueDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterDto registerDto) {
        RegisterResponseDto responseDto = loginService.userRegister(registerDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/home")
    public String getFlag(){
        return "Login Success";
    }
}
