package com.example.login.domain.dto;

import lombok.Builder;
import lombok.Setter;

@Setter
@Builder
public class LoginResponseDto {
    private String loginId;

    private String username;

    private String token;

    private Long expireTime;

    private String refreshToken;

    private String role;
}
