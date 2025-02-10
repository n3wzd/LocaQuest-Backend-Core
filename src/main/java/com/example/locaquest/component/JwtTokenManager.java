package com.example.locaquest.component;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import com.example.locaquest.exception.TokenException;

@Component
public class JwtTokenManager {

    @Value("${jwt.key.login}")
    private String JWT_KEY_LOGIN;  // base64Encoded

    @Value("${jwt.key.auth}")
    private String JWT_KEY_AUTH;  // base64Encoded

    @Value("${jwt.expiration.access}")
    private int JWT_EXPIRATION_ACCESS;

    /*@Value("${jwt.expiration.refresh}")
    private int JWT_EXPIRATION_REFRESH;*/

    @Value("${jwt.expiration.auth}")
    private int JWT_EXPIRATION_AUTH;

    private JwtBuilder generateToken(String secretKey, int validityInMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, validityInMinutes);
        Date validity = calendar.getTime();
        Date now = new Date();

        JwtBuilder token = Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey);
        return token;
    }

    private boolean validateToken(String token, String secretKey) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private String getSubject(String token, String secretKey) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    private Claims getClaims(String token, String secretKey) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    private boolean validateAuthToken(String token) {
        return validateToken(token, JWT_KEY_AUTH);
    }

    public boolean validateLoginToken(String token) {
        return validateToken(token, JWT_KEY_LOGIN);
    }

    public String generateAuthToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        JwtBuilder token = generateToken(JWT_KEY_AUTH, JWT_EXPIRATION_AUTH);
        return token.setClaims(claims).compact();
    }

    public String generateLoginToken(String userId, String name) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("name", name);
        JwtBuilder token = generateToken(JWT_KEY_LOGIN, JWT_EXPIRATION_ACCESS);
        return token.setClaims(claims).compact();
    }

    public String getEmailByAuthToken(String token) {
        return getSubject(token, JWT_KEY_AUTH);
    }

    public String getUserIdByLoginToken(String token) {
        return getSubject(token, JWT_KEY_LOGIN);
    }

    public Claims getClaimsByLoginToken(String token) {
        return getClaims(token, JWT_KEY_LOGIN);
    }

    public void validateAuthTokenWithException(String token) {
        if(token == null || !validateAuthToken(token)) {
            throw new TokenException("Invalid Auth Token");
        }
    }
}
