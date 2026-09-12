package com.aurora.parking.user.application.dto;

import com.aurora.parking.role.domain.Permission;
import com.aurora.parking.role.domain.RoleCode;
import com.aurora.parking.role.domain.RolePermissions;
import com.aurora.parking.user.domain.User;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String fullName,
        String email,
        RoleCode role,
        Set<Permission> permissions,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.id(), user.username(), user.fullName(), user.email(), user.role(),
                RolePermissions.of(user.role()), user.active(), user.createdAt(), user.updatedAt()
        );
    }
}