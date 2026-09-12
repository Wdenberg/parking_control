package com.aurora.parking.camera.application.usecase;

import com.aurora.parking.camera.application.port.CameraRepositoryPort;
import com.aurora.parking.camera.domain.Camera;
import com.aurora.parking.camera.domain.CameraOperation;
import com.aurora.parking.execption.DuplicateCameraCodeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class CreateCameraUseCase {

    private final CameraRepositoryPort repository;
    private final Clock clock;

    public CreateCameraUseCase(CameraRepositoryPort repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Transactional
    public Camera execute(String code, String host, CameraOperation operation, String description) {
        if (repository.existsByCode(code)) {
            throw new DuplicateCameraCodeException(code);
        }
        Instant now = Instant.now(clock);
        Camera camera = Camera.create(UUID.randomUUID(), code, host, operation, description, now);
        return repository.save(camera);
    }
}