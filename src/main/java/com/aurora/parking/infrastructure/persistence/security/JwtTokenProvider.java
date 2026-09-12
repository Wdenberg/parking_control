package com.aurora.parking.infrastructure.persistence.security;

import com.aurora.parking.execption.JwtValidationException;
import com.aurora.parking.security.application.JwtTokenPort;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Implementação com JJWT (io.jsonwebtoken), conforme stack oficial (SPEC §2).
 * NUNCA logar o token completo — apenas eventos de sucesso/falha (§34).
 */
@Component
public class JwtTokenProvider implements JwtTokenPort {

    private final SecretKey accessKey;
    private final JwtProperties properties;

    public JwtTokenProvider(JwtProperties properties) {
        this.properties = properties;
        this.accessKey = Keys.hmacShaKeyFor(properties.getAccessSecret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateAccessToken(UUID userId, String username, String role) {
        Instant now = Instant.now();
        Instant expiry = now.plus(Duration.ofMinutes(properties.getAccessExpiresMinutes()));

        return Jwts.builder()
                .subject(userId.toString())
                .claim("username", username)
                .claim("role", role)
                .issuer(properties.getIssuer())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(accessKey)
                .compact();
    }

    @Override
    public DecodedAccessToken validateAccessToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(accessKey)
                    .requireIssuer(properties.getIssuer())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            UUID userId = UUID.fromString(claims.getSubject());
            String username = claims.get("username", String.class);
            String role = claims.get("role", String.class);
            Instant expiresAt = claims.getExpiration().toInstant();

            return new DecodedAccessToken(userId, username, role, expiresAt);
        } catch (ExpiredJwtException e) {
            throw new JwtValidationException("Token expirado");
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtValidationException("Token inválido");
        }
    }
}