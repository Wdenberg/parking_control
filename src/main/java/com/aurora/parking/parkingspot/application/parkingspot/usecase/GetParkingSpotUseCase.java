package com.aurora.parking.parkingspot.application.parkingspot.usecase;

import com.aurora.parking.execption.ParkingSpotNotFoundException;
import com.aurora.parking.parkingspot.application.parkingspot.port.ParkingSpotRepositoryPort;
import com.aurora.parking.parkingspot.domain.ParkingSpot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GetParkingSpotUseCase {

    private final ParkingSpotRepositoryPort repository;

    public GetParkingSpotUseCase(ParkingSpotRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public ParkingSpot execute(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ParkingSpotNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public boolean isCurrentlyOccupied(UUID id) {
        return repository.isCurrentlyOccupied(id);
    }
}