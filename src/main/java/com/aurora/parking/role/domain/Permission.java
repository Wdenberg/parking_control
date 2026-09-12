package com.aurora.parking.role.domain;

/**
 * Catálogo fechado de permissions (AGENT.md §28).
 * Não é dinâmico — mudança aqui é mudança de contrato.
 */
public enum Permission {
    DASHBOARD_READ,
    APARTMENT_READ, APARTMENT_WRITE,
    PARKING_READ, PARKING_WRITE,
    VEHICLE_READ, VEHICLE_WRITE,
    MOVEMENT_READ, MOVEMENT_MANUAL_WRITE, MOVEMENT_OVERRIDE,
    CAMERA_READ, CAMERA_WRITE, CAMERA_TEST,
    USER_READ, USER_WRITE,
    ROLE_READ, ROLE_WRITE,
    REPORT_READ,
    AUDIT_READ
}