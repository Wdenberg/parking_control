package com.aurora.parking.camera.application.usecase;

import com.aurora.parking.camera.application.port.CameraRepositoryPort;
import com.aurora.parking.camera.domain.Camera;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListCamerasUseCase {

    private final CameraRepositoryPort repository;

    public ListCamerasUseCase(CameraRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Camera> execute(Pageable pageable, Boolean enabledFilter) {
        return repository.findAll(pageable, enabledFilter);
    }
}