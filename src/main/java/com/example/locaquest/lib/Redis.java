package com.example.locaquest.lib;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Redis {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${jwt.expiration.auth}")
    private int jwtExpirationAuth;

    public <T> void save(String key, T value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public <T> T get(String key, Class<T> clazz) {
        Object value = clazz.cast(redisTemplate.opsForValue().get(key));
        if (value == null) {
            return null;
        }
        return objectMapper.convertValue(value, clazz);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}

