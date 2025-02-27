package com.example.locaquest.component;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.example.locaquest.lib.Jwt;
import com.example.locaquest.exception.TokenException;

@Component
@RequiredArgsConstructor
public class TokenComponent {
	
    @Value("${jwt.key.login}")
    private String JWT_KEY_LOGIN;

    @Value("${jwt.key.auth}")
    private String JWT_KEY_AUTH;

    @Value("${jwt.expiration.access}")
    private int JWT_EXPIRATION_ACCESS;

    @Value("${jwt.expiration.auth}")
    private int JWT_EXPIRATION_AUTH;

    private boolean validateAuthToken(String token) {
        return Jwt.verify(token, JWT_KEY_AUTH);
    }

    public boolean validateLoginToken(String token) {
        return Jwt.verify(token, JWT_KEY_LOGIN);
    }

    public String generateAuthToken(String email) {
        return Jwt.generateToken(JWT_KEY_AUTH, JWT_EXPIRATION_AUTH, email, Map.of( ));
    }
    
    public String generateLoginToken(String userId, String name) {
    	return Jwt.generateToken(JWT_KEY_LOGIN, JWT_EXPIRATION_ACCESS, userId, Map.of( "name", name));
    }

    public String getEmailByAuthToken(String token) {
        return Jwt.getSubject(token, JWT_KEY_AUTH);
    }

    public String getUserIdByLoginToken(String token) {
        return Jwt.getSubject(token, JWT_KEY_LOGIN);
    }

    public void validateAuthTokenWithException(String token) {
        if(token == null || !validateAuthToken(token)) {
            throw new TokenException("Invalid Auth Token");
        }
    }
}
