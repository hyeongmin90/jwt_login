package com.example.login.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponseDto {

    private String loginId;

    private String username;

    public RegisterResponseDto form(RegisterDto registerDto){
        RegisterResponseDto responseDto = new RegisterResponseDto();
        responseDto.setUsername(registerDto.getUsername());
        responseDto.setLoginId(registerDto.getLoginId());
        return responseDto;
    }
}
