package com.example.locaquest.lib;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Jwt {
    public String generateToken(String key, int validityInMinutes, String subject, Map<String, String> params) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, validityInMinutes);
        Date validity = calendar.getTime();
        Date now = new Date();

        Claims claims = Jwts.claims().setSubject(subject);
        for (Map.Entry<String, String> entry : params.entrySet()) {
        	claims.put(entry.getKey() , entry.getValue());
        }
        
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, key)   // key is base64Encoded
                .setClaims(claims)
                .compact();
    }
    
    public boolean verify(String token, String key) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
  
    public String getSubject(String token, String key) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    public Claims getClaims(String token, String key) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}
