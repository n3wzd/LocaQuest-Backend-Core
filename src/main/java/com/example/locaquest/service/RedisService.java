package com.example.locaquest.service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.locaquest.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private final String preregisterKey = "preregisterKey";
    private final String changePasswordKey = "changePasswordKey";
    private final String authTokenKey = "authTokenKey";
    private final String userAchievementKey = "userAchievementKey";

    @Value("${jwt.expiration.auth}")
    private int jwtExpirationAuth;

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

    public void saveAuthToken(String token) {
        save(authTokenKey + token, token, jwtExpirationAuth, TimeUnit.MINUTES);
    }

    public String getAuthToken(String token) {
        return get(authTokenKey + token, String.class);
    }

    public void saveUserAchievement(int userId, Map<String, String> achievementSet) {
        save(userAchievementKey + userId, achievementSet, 6, TimeUnit.HOURS);
    }

    public Map<String, String> getUserAchievement(int userId) {
        return get(userAchievementKey + userId, Map.class);
    }
}
