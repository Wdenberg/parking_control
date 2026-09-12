package com.aurora.parking.infrastructure.persistence.camera;


import com.aurora.parking.camera.application.port.CameraRepositoryPort;
import com.aurora.parking.camera.domain.Camera;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CameraRepositoryAdapter implements CameraRepositoryPort {

    private final SpringDataCameraJpaRepository jpaRepository;

    public CameraRepositoryAdapter(SpringDataCameraJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Camera save(Camera camera) {
        return toDomain(jpaRepository.save(toEntity(camera)));
    }

    @Override
    public Optional<Camera> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Camera> findByCode(String code) {
        return jpaRepository.findByCode(code).map(this::toDomain);
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }

    @Override
    public boolean existsByCodeAndIdNot(String code, UUID excludingId) {
        return jpaRepository.existsByCodeAndIdNot(code, excludingId);
    }

    @Override
    public Page<Camera> findAll(Pageable pageable, Boolean enabledFilter) {
        Page<CameraJpaEntity> page = (enabledFilter == null)
                ? jpaRepository.findAll(pageable)
                : jpaRepository.findAllByEnabled(enabledFilter, pageable);
        return page.map(this::toDomain);
    }

    private CameraJpaEntity toEntity(Camera camera) {
        return new CameraJpaEntity(
                camera.id(), camera.code(), camera.host(), camera.operation(),
                camera.description(), camera.enabled(), camera.createdAt(), camera.updatedAt()
        );
    }

    private Camera toDomain(CameraJpaEntity entity) {
        return new Camera(
                entity.getId(), entity.getCode(), entity.getHost(), entity.getOperation(),
                entity.getDescription(), entity.isEnabled(), entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }
}