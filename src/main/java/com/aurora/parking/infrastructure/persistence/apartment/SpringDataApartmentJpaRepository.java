package com.aurora.parking.infrastructure.persistence.apartment;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataApartmentJpaRepository extends JpaRepository<ApartmentJpaEntity, UUID> {

    boolean existsByBlockAndNumber(String block, String number);

    boolean existsByBlockAndNumberAndIdNot(String block, String number, UUID id);

    Page<ApartmentJpaEntity> findAllByActive(boolean active, Pageable pageable);
}