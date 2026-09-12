package com.aurora.parking.infrastructure.persistence.parkingspot;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface SpringDataParkingSpotJpaRepository extends JpaRepository<ParkingSpotJpaEntity, UUID> {

    boolean existsByIdentifier(String identifier);

    boolean existsByIdentifierAndIdNot(String identifier, UUID id);

    Page<ParkingSpotJpaEntity> findAllByApartmentId(UUID apartmentId, Pageable pageable);

    Page<ParkingSpotJpaEntity> findAllByEnabled(boolean enabled, Pageable pageable);

    Page<ParkingSpotJpaEntity> findAllByApartmentIdAndEnabled(UUID apartmentId, boolean enabled, Pageable pageable);

    /**
     * Verifica ocupação via existência de Movement OPEN, sem trazer o Movement inteiro.
     * Assume tabela movements(parking_spot_id, status) — consistente com V5__create_movements.sql.
     */
    @Query("""
           SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END
           FROM MovementJpaEntity m
           WHERE m.parkingSpotId = :parkingSpotId AND m.status = 'OPEN'
           """)
    boolean existsOpenMovementForSpot(@Param("parkingSpotId") UUID parkingSpotId);
}