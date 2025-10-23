package com.example.login.infra;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        // Key: StringRedisSerializer 사용
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // Value: StringRedisSerializer 사용 (객체를 저장할 경우 new GenericJackson2JsonRedisSerializer())
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        // Hash의 Key와 Value 직렬화 방식 설정
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}
