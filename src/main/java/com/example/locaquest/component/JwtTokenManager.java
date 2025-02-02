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
    private String jwtKeyLogin;

    @Value("${jwt.key.auth}")
    private String jwtKeyAuth;

    @Value("${jwt.expiration.access}")
    private int jwtExpirationAccess;

    /*@Value("${jwt.expiration.refresh}")
    private int jwtExpirationRefresh;*/

    @Value("${jwt.expiration.auth}")
    private int jwtExpirationAuth;

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
        return validateToken(token, jwtKeyAuth);
    }

    public boolean validateLoginToken(String token) {
        return validateToken(token, jwtKeyLogin);
    }

    public String generateAuthToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        JwtBuilder token = generateToken(jwtKeyAuth, jwtExpirationAuth);
        return token.setClaims(claims).compact();
    }

    public String generateLoginToken(String userId, String name) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("name", name);
        JwtBuilder token = generateToken(jwtKeyLogin, jwtExpirationAccess);
        return token.setClaims(claims).compact();
    }

    public String getEmailByAuthToken(String token) {
        return getSubject(token, jwtKeyAuth);
    }

    public String getUserIdByLoginToken(String token) {
        return getSubject(token, jwtKeyLogin);
    }

    public Claims getClaimsByLoginToken(String token) {
        return getClaims(token, jwtKeyLogin);
    }

    public void validateAuthTokenWithException(String token) {
        if(token == null || !validateAuthToken(token)) {
            throw new TokenException("Invalid Auth Token");
        }
    }
}
