package com.aurora.parking.role.domain;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * STATUS: NÃO CONFIRMADO.
 * O AGENT.md lista roles e permissions separadamente, sem mapear
 * explicitamente quem tem o quê. Este mapeamento é uma proposta
 * razoável e PRECISA de validação humana antes de produção.
 *
 * Evidência necessária: confirmar com o time de negócio/produto
 * qual permission cada role deve ter.
 */
public final class RolePermissions {

    private static final Map<RoleCode, Set<Permission>> MAP = new EnumMap<>(RoleCode.class);

    static {
        MAP.put(RoleCode.SUPER_ADMIN, EnumSet.allOf(Permission.class));

        MAP.put(RoleCode.ADMIN, EnumSet.of(
                Permission.DASHBOARD_READ,
                Permission.APARTMENT_READ, Permission.APARTMENT_WRITE,
                Permission.PARKING_READ, Permission.PARKING_WRITE,
                Permission.VEHICLE_READ, Permission.VEHICLE_WRITE,
                Permission.MOVEMENT_READ, Permission.MOVEMENT_MANUAL_WRITE, Permission.MOVEMENT_OVERRIDE,
                Permission.CAMERA_READ, Permission.CAMERA_WRITE, Permission.CAMERA_TEST,
                Permission.USER_READ, Permission.USER_WRITE,
                Permission.ROLE_READ,
                Permission.REPORT_READ,
                Permission.AUDIT_READ
        ));

        MAP.put(RoleCode.OPERATOR, EnumSet.of(
                Permission.DASHBOARD_READ,
                Permission.APARTMENT_READ,
                Permission.PARKING_READ,
                Permission.VEHICLE_READ, Permission.VEHICLE_WRITE,
                Permission.MOVEMENT_READ, Permission.MOVEMENT_MANUAL_WRITE,
                Permission.CAMERA_READ,
                Permission.REPORT_READ
        ));

        MAP.put(RoleCode.ATTENDANT, EnumSet.of(
                Permission.DASHBOARD_READ,
                Permission.APARTMENT_READ,
                Permission.PARKING_READ,
                Permission.VEHICLE_READ,
                Permission.MOVEMENT_READ, Permission.MOVEMENT_MANUAL_WRITE,
                Permission.CAMERA_READ
        ));

        MAP.put(RoleCode.VIEWER, EnumSet.of(
                Permission.DASHBOARD_READ,
                Permission.APARTMENT_READ,
                Permission.PARKING_READ,
                Permission.VEHICLE_READ,
                Permission.MOVEMENT_READ,
                Permission.CAMERA_READ,
                Permission.REPORT_READ
        ));
    }

    private RolePermissions() {
    }

    public static Set<Permission> of(RoleCode role) {
        return MAP.getOrDefault(role, Set.of());
    }
}