package com.aurora.parking.camera.application.usecase;


import com.aurora.parking.camera.application.port.CameraRepositoryPort;
import com.aurora.parking.camera.domain.Camera;
import com.aurora.parking.execption.CameraNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class GetCameraUseCase {

    private final CameraRepositoryPort repository;

    public GetCameraUseCase(CameraRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Camera execute(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new CameraNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Optional<Camera> findByCode(String code) {
        return repository.findByCode(code);
    }
}