package com.aurora.parking.parkingspot.application.parkingspot.dto;

import com.aurora.parking.parkingspot.domain.ParkingSpot;

import java.time.Instant;
import java.util.UUID;

public record ParkingSpotResponse(
        UUID id,
        UUID apartmentId,
        String identifier,
        boolean enabled,
        boolean currentlyOccupied,
        Instant createdAt,
        Instant updatedAt
) {
    public static ParkingSpotResponse from(ParkingSpot spot, boolean currentlyOccupied) {
        return new ParkingSpotResponse(
                spot.id(),
                spot.apartmentId(),
                spot.identifier(),
                spot.enabled(),
                currentlyOccupied,
                spot.createdAt(),
                spot.updatedAt()
        );
    }
}