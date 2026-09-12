package com.aurora.parking.security.application;


import com.aurora.parking.role.domain.Permission;
import com.aurora.parking.role.domain.RoleCode;

import java.util.Set;
import java.util.UUID;

/**
 * Identidade do operador autenticado, extraída do JWT validado.
 * Nunca construído a partir de dados enviados no corpo da requisição.
 */
public record CurrentOperator(
        UUID userId,
        String username,
        RoleCode role,
        Set<Permission> permissions
) {}