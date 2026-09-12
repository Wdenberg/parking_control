package com.aurora.parking.user.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(max = 100) String fullName,
        @Email @Size(max = 100) String email
) {}