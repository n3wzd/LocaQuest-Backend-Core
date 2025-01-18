package com.example.reviewsplash.util;

import jakarta.servlet.http.HttpServletRequest;

public class TokenParser {

    public static String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
