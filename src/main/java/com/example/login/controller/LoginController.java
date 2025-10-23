package com.example.login.controller;

import com.example.login.domain.dto.LoginDto;
import com.example.login.domain.dto.LoginResponseDto;
import com.example.login.domain.dto.RegisterDto;
import com.example.login.domain.dto.RegisterResponseDto;
import com.example.login.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> Login(@RequestBody @Valid LoginDto loginDto) {
        LoginResponseDto responseDto = loginService.userLogin(loginDto);
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
