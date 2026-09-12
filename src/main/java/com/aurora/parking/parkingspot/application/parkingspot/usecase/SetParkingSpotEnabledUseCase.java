package com.aurora.parking.parkingspot.application.parkingspot.usecase;


import com.aurora.parking.execption.ParkingSpotNotFoundException;
import com.aurora.parking.execption.ParkingSpotOccupiedException;
import com.aurora.parking.parkingspot.application.parkingspot.port.ParkingSpotRepositoryPort;
import com.aurora.parking.parkingspot.domain.ParkingSpot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class SetParkingSpotEnabledUseCase {

    private final ParkingSpotRepositoryPort repository;
    private final Clock clock;

    public SetParkingSpotEnabledUseCase(ParkingSpotRepositoryPort repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Transactional
    public ParkingSpot execute(UUID id, boolean enabled) {
        ParkingSpot spot = repository.findById(id)
                .orElseThrow(() -> new ParkingSpotNotFoundException(id));

        // Não é possível desabilitar uma vaga com Movement OPEN (INV-02)
        if (!enabled && repository.isCurrentlyOccupied(id)) {
            throw new ParkingSpotOccupiedException(id);
        }

        Instant now = Instant.now(clock);
        ParkingSpot updated = enabled ? spot.enable(now) : spot.disable(now);
        return repository.save(updated);
    }
}