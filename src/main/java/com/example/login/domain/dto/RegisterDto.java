package com.example.login.domain.dto;

import com.example.login.domain.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto {
    private String loginId;

    private String password;

    private String username;

    private String email;

    private String role;

    public Member toEntity(RegisterDto registerDto, String password){
        Member member = new Member();
        member.setLoginId(registerDto.getLoginId());
        member.setPassword(password);
        member.setUsername(registerDto.getUsername());
        member.setEmail(registerDto.getEmail());
        member.setRole(registerDto.getRole());
        return member;
    }
}
