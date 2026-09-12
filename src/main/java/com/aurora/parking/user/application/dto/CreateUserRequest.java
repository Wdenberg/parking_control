package com.aurora.parking.user.application.dto;

import com.aurora.parking.role.domain.RoleCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank @Size(max = 50) String username,
        @NotBlank @Size(min = 8, max = 128, message = "senha deve ter no mínimo 8 caracteres") String password,
        @Size(max = 100) String fullName,
        @Email @Size(max = 100) String email,
        @NotNull RoleCode role
) {}