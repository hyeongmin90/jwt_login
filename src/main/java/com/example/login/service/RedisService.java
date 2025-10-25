package com.example.login.service;

import com.example.login.infra.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    public String createRefreshTokenWithSave(Long id, String name){
        String refreshToken = jwtUtil.createRefreshToken(id, name);
        setRefreshToken(String.valueOf(id), refreshToken);
        return refreshToken;
    }

    public void setRefreshToken(String key, String value) {
        Duration duration = Duration.ofDays(7);
        redisTemplate.opsForValue().set(key, value, duration);
    }

    public String getRefreshToken(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteRefreshToken(String key) {
        redisTemplate.delete(key);
    }
}
