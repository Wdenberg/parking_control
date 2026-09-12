package com.aurora.parking.infrastructure.persistence.camera;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataCameraJpaRepository extends JpaRepository<CameraJpaEntity, UUID> {

    Optional<CameraJpaEntity> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, UUID id);

    Page<CameraJpaEntity> findAllByEnabled(boolean enabled, Pageable pageable);
}