package com.example.locaquest.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

import com.example.locaquest.model.User;

@Service
public class RedisService {
    private RedisTemplate<String, Object> redisTemplate;
    private ObjectMapper objectMapper;

    private final String preregisterKey = "preregisterKey";
    private final String changePasswordKey = "changePasswordKey";

    public RedisService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    private <T> void save(String key, T value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    private <T> T get(String key, Class<T> clazz) {
        Object value = clazz.cast(redisTemplate.opsForValue().get(key));
        if (value == null) {
            return null;
        }
        return objectMapper.convertValue(value, clazz);
    }

    private void delete(String key) {
        redisTemplate.delete(key);
    }

    public void savePreregisterUser(User user) {
        save(preregisterKey + user.getEmail(), user, 1, TimeUnit.HOURS);
    }

    public User getPreregisterUser(String email) {
        return get(preregisterKey + email, User.class);
    }

    public void deletePreregisterUser(String email) {
        delete(preregisterKey + email);
    }

    public void saveChangePasswordEmail(String email) {
        save(changePasswordKey + email, email, 1, TimeUnit.HOURS);
    }

    public String getChangePasswordEmail(String email) {
        return get(changePasswordKey + email, String.class);
    }

    public void deleteChangePasswordEmail(String email) {
        delete(changePasswordKey + email);
    }
}
