package com.bookstore.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;

@Component
public class JwtUtil {
    private final Key key;
    private final String issuer;
    private final long expiryMinutes;

    public JwtUtil(@Value("${security.jwt.secret}") String secret,
                   @Value("${security.jwt.issuer}") String issuer,
                   @Value("${security.jwt.expiry-minutes}") long expiryMinutes) {
        Key k;
        try {
            if (secret == null || secret.isBlank()) {
                k = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            } else {
                k = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
            }
        } catch (Exception e) {
            k = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        }
        this.key = k;
        this.issuer = issuer;
        this.expiryMinutes = expiryMinutes;
    }

    public String generateToken(String subject, List<String> permissions) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expiryMinutes * 60);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuer(issuer)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .addClaims(Map.of("permissions", permissions))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Key signingKey() {
        return key;
    }
}
