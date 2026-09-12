package com.aurora.parking.role.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RolePermissionsTest {

    @Test
    void superAdminHasEveryPermission() {
        assertTrue(RolePermissions.of(RoleCode.SUPER_ADMIN).containsAll(java.util.Set.of(Permission.values())));
    }

    @Test
    void viewerCannotWriteVehicles() {
        assertTrue(!RolePermissions.of(RoleCode.VIEWER).contains(Permission.VEHICLE_WRITE));
    }
}
