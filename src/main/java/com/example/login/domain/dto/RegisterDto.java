package com.example.login.domain.dto;

import com.example.login.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto {
    private String loginId;

    private String password;

    private String username;

    private String email;

    public User toEntity(RegisterDto registerDto, String password){
        User user = new User();
        user.setLoginId(registerDto.getLoginId());
        user.setPassword(password);
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        return user;
    }
}
