package com.example.login.domain.dto;

import lombok.Builder;
import lombok.Setter;

@Setter
@Builder
public class ReissueResponseDto {
    private String accessToken;

    private String refreshToken;
}
