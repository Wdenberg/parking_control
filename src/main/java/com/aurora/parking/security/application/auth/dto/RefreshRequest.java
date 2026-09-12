package com.aurora.parking.security.application.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
        @NotBlank(message = "refreshToken é obrigatório") String refreshToken
) {}