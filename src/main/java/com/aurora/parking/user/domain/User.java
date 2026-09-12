package com.aurora.parking.user.domain;


import com.aurora.parking.role.domain.Permission;
import com.aurora.parking.role.domain.RoleCode;
import com.aurora.parking.role.domain.RolePermissions;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Usuário do sistema. passwordHash NUNCA é exposto fora do domínio
 * (não existe getter público de senha em texto puro em lugar nenhum).
 */
public record User(
        UUID id,
        String username,
        String passwordHash,
        String fullName,
        String email,
        RoleCode role,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
    public User {
        Objects.requireNonNull(username, "username não pode ser nulo");
        Objects.requireNonNull(passwordHash, "passwordHash não pode ser nulo");
        Objects.requireNonNull(role, "role não pode ser nulo");
        if (username.isBlank()) {
            throw new IllegalArgumentException("username não pode ser vazio");
        }
    }

    public static User create(UUID id, String username, String passwordHash, String fullName,
                              String email, RoleCode role, Instant now) {
        return new User(id, username.trim(), passwordHash, fullName, email, role, true, now, now);
    }

    public User changeRole(RoleCode newRole, Instant now) {
        return new User(id, username, passwordHash, fullName, email, newRole, active, createdAt, now);
    }

    public User updateDetails(String newFullName, String newEmail, Instant now) {
        return new User(id, username, passwordHash, newFullName, newEmail, role, active, createdAt, now);
    }

    public User changePasswordHash(String newPasswordHash, Instant now) {
        return new User(id, username, newPasswordHash, fullName, email, role, active, createdAt, now);
    }

    public User deactivate(Instant now) {
        if (!active) return this;
        return new User(id, username, passwordHash, fullName, email, role, false, createdAt, now);
    }

    public User activate(Instant now) {
        if (active) return this;
        return new User(id, username, passwordHash, fullName, email, role, true, createdAt, now);
    }

    public Set<Permission> permissions() {
        return RolePermissions.of(role);
    }
}