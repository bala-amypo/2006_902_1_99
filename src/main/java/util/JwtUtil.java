package com.example.demo.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtUtil {

    // SAME SECRET USED EVERYWHERE
    private static final String SECRET = "demo_secret_key_123";

    // ================== GENERATE TOKEN ==================
    public String generateToken(String email, Long userId, Set<String> roles) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("userId", userId);
        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    // ================== VALIDATE TOKEN ==================
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ================== EXTRACT EMAIL ==================
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // ================== EXTRACT CLAIMS ==================
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
