package com.aurora.parking.camera.application.dto;

import com.aurora.parking.camera.domain.Camera;
import com.aurora.parking.camera.domain.CameraOperation;

import java.time.Instant;
import java.util.UUID;

public record CameraResponse(
        UUID id,
        String code,
        String host,
        CameraOperation operation,
        String description,
        boolean enabled,
        Instant createdAt,
        Instant updatedAt
) {
    public static CameraResponse from(Camera camera) {
        return new CameraResponse(
                camera.id(), camera.code(), camera.host(), camera.operation(),
                camera.description(), camera.enabled(), camera.createdAt(), camera.updatedAt()
        );
    }
}