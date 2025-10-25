package com.example.login.service;

import com.example.login.domain.Member;
import com.example.login.domain.MemberRepository;
import com.example.login.domain.dto.*;
import com.example.login.infra.JwtUtil;
import com.example.login.infra.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final JwtUtil jwtUtil;

    public LoginResponseDto userLogin(LoginDto loginDto){
        Optional<Member> checkUser = memberRepository.findByLoginId(loginDto.getLoginId());
        if(checkUser.isEmpty()){
            throw new BadCredentialsException("ID or Password is not correct");
        }
        Member member = checkUser.get();

        if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())){
            throw new BadCredentialsException("ID or Password is not correct");
        }
        String accessToken = jwtUtil.createAccessToken(member.getUserId(), member.getRole());
        String refreshToken = redisService.createRefreshTokenWithSave(member.getUserId(), member.getRole());

        return LoginResponseDto.builder()
                .loginId(loginDto.getLoginId())
                .username(member.getUsername())
                .token(accessToken)
                .expireTime(30 * 60L)
                .refreshToken(refreshToken)
                .build();
    }

    public RegisterResponseDto userRegister(RegisterDto registerDto){
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());
        Member member = registerDto.toEntity(registerDto, encodedPassword);
        memberRepository.save(member);
        return new RegisterResponseDto().form(registerDto);
    }

    public ReissueResponseDto userReissue(ReissueDto reissueDto){
        String refreshToken = reissueDto.getRefreshToken();
        if (!jwtUtil.validateRefreshToken(refreshToken)){
            throw new BadCredentialsException("사용 불가한 Refresh Token");
        }
        UserInfo userInfo = jwtUtil.getInfoFromToken(refreshToken);

        Long userId = userInfo.getId();
        String storedRefreshToken = redisService.getRefreshToken(String.valueOf(userId));

        if(storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)){
            throw new BadCredentialsException("사용 불가한 Refresh Token");
        }

        String username = userInfo.getName();
        String newAccessToken = jwtUtil.createAccessToken(userId, username);
        String newRefreshToken = redisService.createRefreshTokenWithSave(userId, username);

        return ReissueResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
