package com.example.login.domain;

import com.example.login.domain.dto.RegisterDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Setter
@Getter
public class User {

    @Id
    private Long userId;

    private String loginId;

    private String password;

    private String username;

    private String email;

    private String role;
}
