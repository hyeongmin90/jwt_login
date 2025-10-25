package com.example.login.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
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
