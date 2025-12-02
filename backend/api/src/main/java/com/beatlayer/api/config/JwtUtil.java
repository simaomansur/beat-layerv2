package com.beatlayer.api.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

  // Dev-only in-memory key. It stays the same for this JVM,
  // so tokens work for as long as the app is running.
  private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  // 7 days
  private final long expirationMs = 1000L * 60 * 60 * 24 * 7;

  public String createToken(UUID userId, String email) {
    long now = System.currentTimeMillis();

    return Jwts.builder()
        .setSubject(userId.toString())
        .claim("email", email)
        .setIssuedAt(new Date(now))
        .setExpiration(new Date(now + expirationMs))
        .signWith(key)
        .compact();
  }

  public UUID extractUserId(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();

    return UUID.fromString(claims.getSubject());
  }
}
