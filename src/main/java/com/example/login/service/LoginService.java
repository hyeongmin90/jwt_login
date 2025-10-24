package com.example.login.service;

import com.example.login.domain.User;
import com.example.login.domain.UserRepository;
import com.example.login.domain.dto.*;
import com.example.login.infra.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
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
        String accessToken = jwtUtil.createAccessToken(user.getUserId(), user.getRole());

        String refreshToken = jwtUtil.createRefreshToken(user.getUserId(), user.getRole());
        String id = String.valueOf(user.getUserId());
        Duration refreshTokenValidity = Duration.ofDays(7);
        redisService.setRefreshToken(id, refreshToken, refreshTokenValidity);
        
        return LoginResponseDto.builder()
                .loginId(loginDto.getLoginId())
                .username(user.getUsername())
                .token(accessToken)
                .expireTime(30 * 60L)
                .refreshToken(refreshToken)
                .build();
    }

    public RegisterResponseDto userRegister(RegisterDto registerDto){
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());
        User user = registerDto.toEntity(registerDto, encodedPassword);
        userRepository.save(user);
        return new RegisterResponseDto().form(registerDto);
    }

    public ReissueResponseDto userReissue(ReissueDto reissueDto){
        String refreshToken = reissueDto.getRefreshToken();
        if (!jwtUtil.validateRefreshToken(refreshToken)){

        }

        ReissueResponseDto responseDto = new ReissueResponseDto();
        return responseDto;
    }
}
