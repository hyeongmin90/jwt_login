package com.example.login.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReissueResponseDto {
    private String accessToken;

    private String refreshToken;
}
