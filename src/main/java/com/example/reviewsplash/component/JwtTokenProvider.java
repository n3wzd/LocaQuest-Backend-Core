package com.example.reviewsplash.component;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

    @Value("${jwt.key.login}")
    private String jwtKeyLogin;

    @Value("${jwt.key.auth}")
    private String jwtKeyAuth;

    @Value("${jwt.expiration.access}")
    private int jwtExpirationAccess;

    @Value("${jwt.expiration.refresh}")
    private int jwtExpirationRefresh;

    @Value("${jwt.expiration.auth}")
    private int jwtExpirationAuth;

    private String generateToken(String subject, String secretKey, int validityInMinutes) {
        Claims claims = Jwts.claims().setSubject(subject);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, validityInMinutes);
        Date validity = calendar.getTime();
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
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
            return "";
        }
    }

    public String generateAuthToken(String email) {
        return generateToken(email, jwtKeyAuth, jwtExpirationAuth);
    }

    public String generateLoginToken(String userId) {
        return generateToken(userId, jwtKeyLogin, jwtExpirationAccess);
    }

    public boolean validateAuthToken(String token) {
        return validateToken(token, jwtKeyAuth);
    }

    public boolean validateLoginToken(String token) {
        return validateToken(token, jwtKeyLogin);
    }

    public String getEmailByAuthToken(String token) {
        return getSubject(token, jwtKeyAuth);
    }

    public String getUserIdByLoginToken(String token) {
        return getSubject(token, jwtKeyLogin);
    }
}
