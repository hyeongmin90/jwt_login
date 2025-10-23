package com.example.login.domain.dto;

import lombok.Setter;

@Setter
public class LoginResponseDto {
    private String loginId;

    private String username;

    private String token;

    private Long expireTime;

    private String refreshToken;

    private String role;
}
