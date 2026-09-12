package com.aurora.parking.user.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank @Size(min = 8, max = 128, message = "senha deve ter no mínimo 8 caracteres") String newPassword
) {}