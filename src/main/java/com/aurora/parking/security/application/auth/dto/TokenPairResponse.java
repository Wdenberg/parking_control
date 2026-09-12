package com.aurora.parking.security.application.auth.dto;

public record TokenPairResponse(
        String accessToken,
        String refreshToken,
        long accessExpiresInSeconds
) {}