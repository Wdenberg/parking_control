package com.aurora.parking.vehicle.application.vehicle.dto;

import com.aurora.parking.vehicle.domain.Vehicle;

import java.time.Instant;
import java.util.UUID;

public record VehicleResponse(
        UUID id,
        UUID apartmentId,
        String plate,
        String model,
        String color,
        boolean active,
        boolean currentlyParked,
        Instant createdAt,
        Instant updatedAt
) {
    public static VehicleResponse from(Vehicle vehicle, boolean currentlyParked) {
        return new VehicleResponse(
                vehicle.id(), vehicle.apartmentId(), vehicle.plate(), vehicle.model(),
                vehicle.color(), vehicle.active(), currentlyParked, vehicle.createdAt(), vehicle.updatedAt()
        );
    }
}
