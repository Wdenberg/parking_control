package com.aurora.parking.parkingspot.application.parkingspot.port;

import com.aurora.parking.parkingspot.domain.ParkingSpot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ParkingSpotRepositoryPort {

    ParkingSpot save(ParkingSpot parkingSpot);

    Optional<ParkingSpot> findById(UUID id);

    boolean existsByIdentifier(String identifier);

    boolean existsByIdentifierAndIdNot(String identifier, UUID excludingId);

    /**
     * true se existir Movement com status OPEN para esta vaga.
     * Implementado via query em infrastructure (JOIN com movements).
     */
    boolean isCurrentlyOccupied(UUID parkingSpotId);

    Page<ParkingSpot> findAll(Pageable pageable, UUID apartmentIdFilter, Boolean enabledFilter);
}