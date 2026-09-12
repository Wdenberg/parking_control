package com.aurora.parking.security.application.auth.dto;

import com.aurora.parking.role.domain.Permission;
import com.aurora.parking.role.domain.RoleCode;

import java.util.Set;
import java.util.UUID;

public record CurrentOperatorResponse(
        UUID id,
        String username,
        RoleCode role,
        Set<Permission> permissions
) {}