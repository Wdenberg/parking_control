package com.aurora.parking.vehicle.domain;


import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record Vehicle(
        UUID id,
        UUID apartmentId,
        String plate,
        String model,
        String color,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
    public Vehicle {
        Objects.requireNonNull(apartmentId, "apartmentId não pode ser nulo");
        Objects.requireNonNull(plate, "plate não pode ser nula");
        // plate já deve chegar normalizada — validação de formato é responsabilidade do caller (PlateNormalizer)
    }

    public static Vehicle create(UUID id, UUID apartmentId, String normalizedPlate,
                                 String model, String color, Instant now) {
        return new Vehicle(id, apartmentId, normalizedPlate, model, color, true, now, now);
    }

    public Vehicle reassign(UUID newApartmentId, Instant now) {
        return new Vehicle(id, newApartmentId, plate, model, color, active, createdAt, now);
    }

    public Vehicle updateDetails(String newModel, String newColor, Instant now) {
        return new Vehicle(id, apartmentId, plate, newModel, newColor, active, createdAt, now);
    }

    public Vehicle deactivate(Instant now) {
        if (!active) {
            return this;
        }
        return new Vehicle(id, apartmentId, plate, model, color, false, createdAt, now);
    }

    public Vehicle activate(Instant now) {
        if (active) {
            return this;
        }
        return new Vehicle(id, apartmentId, plate, model, color, true, createdAt, now);
    }
}