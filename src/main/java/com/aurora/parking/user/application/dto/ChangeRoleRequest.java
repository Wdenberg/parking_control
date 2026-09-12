package com.aurora.parking.user.application.dto;

import com.aurora.parking.role.domain.RoleCode;
import jakarta.validation.constraints.NotNull;

public record ChangeRoleRequest(@NotNull RoleCode role) {}