package com.aurora.parking.camera.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Registro administrativo de câmera.
 * NÃO é a fonte de verdade da conexão física — isso continua em
 * HikvisionProperties/HikvisionCameraConfig (env vars, credenciais).
 * Este registro serve para administração, dashboard e auditoria.
 */
public record Camera(
        UUID id,
        String code,            // "LPR-01", "LPR-02" — deve casar com HikvisionCameraConfig
        String host,             // metadado informativo, não usado para conectar
        CameraOperation operation,
        String description,
        boolean enabled,         // desabilitar aqui é sinal administrativo, não desliga o stream sozinho
        Instant createdAt,
        Instant updatedAt
) {
    public Camera {
        Objects.requireNonNull(code, "code não pode ser nulo");
        Objects.requireNonNull(operation, "operation não pode ser nulo");
        if (code.isBlank()) {
            throw new IllegalArgumentException("code não pode ser vazio");
        }
    }

    public static Camera create(UUID id, String code, String host, CameraOperation operation,
                                String description, Instant now) {
        return new Camera(id, code.trim().toUpperCase(), host, operation, description, true, now, now);
    }

    public Camera updateDetails(String newHost, String newDescription, Instant now) {
        return new Camera(id, code, newHost, operation, newDescription, enabled, createdAt, now);
    }

    public Camera disable(Instant now) {
        if (!enabled) {
            return this;
        }
        return new Camera(id, code, host, operation, description, false, createdAt, now);
    }

    public Camera enable(Instant now) {
        if (enabled) {
            return this;
        }
        return new Camera(id, code, host, operation, description, true, createdAt, now);
    }
}