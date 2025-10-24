package com.example.login.domain.dto;

import lombok.Setter;

@Setter
public class ReissueResponseDto {
    private String accessToken;

    private String refreshToken;
}
