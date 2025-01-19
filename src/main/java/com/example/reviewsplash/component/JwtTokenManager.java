package com.example.reviewsplash.component;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import com.example.reviewsplash.exception.TokenException;

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

    private final String redirectUrlClaimName = "redirectUrl";

    private String generateToken(String subject, String secretKey, int validityInMinutes, String redirectUrl) {
        Claims claims = Jwts.claims().setSubject(subject);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, validityInMinutes);
        Date validity = calendar.getTime();
        Date now = new Date();

        JwtBuilder token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey);
        if(redirectUrl != null) {
            token.claim(redirectUrlClaimName, redirectUrl);
        }
        return token.compact();
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

    public String generateAuthToken(String email, String redirectUrl) {
        return generateToken(email, jwtKeyAuth, jwtExpirationAuth, redirectUrl);
    }

    public String generateLoginToken(String userId) {
        return generateToken(userId, jwtKeyLogin, jwtExpirationAccess, null);
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

    public String getRedirectUrlByAuthToken(String token) {
        Claims claims = getClaims(token, jwtKeyAuth);
        if(claims != null) {
            return claims.get(redirectUrlClaimName, String.class);
        } else {
            return null;
        }
    }

    public void validateAuthTokenWithException(String token) {
        if(token == null || validateAuthToken(token)) {
            throw new TokenException("Invalid Auth Token");
        }
    }
}
