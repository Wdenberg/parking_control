package com.aurora.parking.camera.application.port;


import com.aurora.parking.camera.domain.Camera;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CameraRepositoryPort {

    Camera save(Camera camera);

    Optional<Camera> findById(UUID id);

    Optional<Camera> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, UUID excludingId);

    Page<Camera> findAll(Pageable pageable, Boolean enabledFilter);
}