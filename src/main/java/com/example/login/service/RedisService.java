package com.example.login.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public void setRefreshToken(String key, String value, Duration duration) {
        redisTemplate.opsForValue().set(key, value, duration);
    }

    public String getRefreshToken(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
