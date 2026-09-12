package com.aurora.parking.infrastructure.persistence.vehicle;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataVehicleJpaRepository extends JpaRepository<VehicleJpaEntity, UUID> {

    Optional<VehicleJpaEntity> findByPlate(String plate);

    boolean existsByPlate(String plate);

    boolean existsByPlateAndIdNot(String plate, UUID id);

    Page<VehicleJpaEntity> findAllByApartmentId(UUID apartmentId, Pageable pageable);

    Page<VehicleJpaEntity> findAllByActive(boolean active, Pageable pageable);

    Page<VehicleJpaEntity> findAllByApartmentIdAndActive(UUID apartmentId, boolean active, Pageable pageable);

    /** Assume tabela movements(vehicle_id, status) — consistente com V5__create_movements.sql. */
    @Query("""
           SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END
           FROM MovementJpaEntity m
           WHERE m.vehicleId = :vehicleId AND m.status = 'OPEN'
           """)
    boolean existsOpenMovementForVehicle(@Param("vehicleId") UUID vehicleId);
}