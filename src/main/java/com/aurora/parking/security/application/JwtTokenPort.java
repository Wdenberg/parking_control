package com.aurora.parking.security.application;

import java.time.Instant;
import java.util.UUID;

public interface JwtTokenPort {

    String generateAccessToken(UUID userId, String username, String role);

    /** @return payload decodificado, ou lança JwtValidationException se inválido/expirado. */
    DecodedAccessToken validateAccessToken(String token);

    record DecodedAccessToken(UUID userId, String username, String role, Instant expiresAt) {}
}