package com.ms.chat.application.Utils;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private String SECRET_KEY = "aVeryLongSecretKeyThatIsAtLeast256BitsLongAndSecure";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username, String userId, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("userId", userId); // Add userId to claims

        // Add email only if it's not null or empty
        if (email != null && !email.isEmpty()) {
            claims.put("email", email); // Add email to claims if provided
        }

        return createToken(claims, username); // Create the token with additional claims
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .header().empty().add("typ", "JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSigningKey())
                .compact();
    }


    // Method to extract email from the token
    public String extractEmail(String token) {
        Claims claims = extractAllClaims(token);
        return (String) claims.get("email"); // Get the email from the claims
    }

    // Method to extract userId from the token
    public String extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return (String) claims.get("userId"); // Get the userId from the claims
    }


    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

}



