package com.example.login.service;

import com.example.login.domain.User;
import com.example.login.domain.UserRepository;
import com.example.login.domain.dto.LoginDto;
import com.example.login.domain.dto.LoginResponseDto;
import com.example.login.domain.dto.RegisterDto;
import com.example.login.domain.dto.RegisterResponseDto;
import com.example.login.infra.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponseDto userLogin(LoginDto loginDto){
        Optional<User> checkUser = userRepository.findByLoginId(loginDto.getLoginId());
        if(checkUser.isEmpty()){
            throw new BadCredentialsException("ID or Password is not correct");
        }
        User user = checkUser.get();

        if (!passwordEncoder.matches(user.getPassword(), loginDto.getPassword())){
            throw new BadCredentialsException("ID or Password is not correct");
        }
        String token = jwtUtil.createAccessToken(user.getUserId(), user.getUsername());
        String refreshToken = jwtUtil.createRefreshToken(user.getUserId(), user.getUsername());
        LoginResponseDto responseDto = new LoginResponseDto();
        
        responseDto.setLoginId(loginDto.getLoginId());
        responseDto.setUsername(user.getUsername());
        responseDto.setToken(token);
        responseDto.setExpireTime(30 * 60L);
        responseDto.setRefreshToken(refreshToken);
        
        return responseDto;
    }

    public RegisterResponseDto userRegister(RegisterDto registerDto){
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());
        User user = registerDto.toEntity(registerDto, encodedPassword);
        userRepository.save(user);
        return new RegisterResponseDto().form(registerDto);
    }
}
