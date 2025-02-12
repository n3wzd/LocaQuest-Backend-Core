package com.example.locaquest.component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.locaquest.model.User;
import com.example.locaquest.lib.Redis;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisComponent {
	private final Redis redis;
	
    private final String preregisterKey = "preregister:";
    private final String changePasswordKey = "changePassword:";
    private final String authTokenKey = "authToken:";
    private final String userAchievementKey = "userAchievement:";
    
    @Value("${jwt.expiration.auth}")
    private int jwtExpirationAuth;

    public void savePreregisterUser(User user) {
    	redis.save(preregisterKey + user.getEmail(), user, 1, TimeUnit.HOURS);
    }

    public User getPreregisterUser(String email) {
        return redis.get(preregisterKey + email, User.class);
    }

    public void deletePreregisterUser(String email) {
    	redis.delete(preregisterKey + email);
    }

    public void saveChangePasswordEmail(String email) {
    	redis.save(changePasswordKey + email, email, 1, TimeUnit.HOURS);
    }

    public String getChangePasswordEmail(String email) {
        return redis.get(changePasswordKey + email, String.class);
    }

    public void deleteChangePasswordEmail(String email) {
    	redis.delete(changePasswordKey + email);
    }

    public void saveAuthToken(String token) {
    	redis.save(authTokenKey + token, token, jwtExpirationAuth, TimeUnit.MINUTES);
    }

    public String getAuthToken(String token) {
        return redis.get(authTokenKey + token, String.class);
    }

    public void saveUserAchievement(int userId, Map<String, String> achievementSet) {
    	redis.save(userAchievementKey + userId, achievementSet, 6, TimeUnit.HOURS);
    }

    public Map<String, String> getUserAchievement(int userId) {
        return redis.get(userAchievementKey + userId, Map.class);
    }
}
