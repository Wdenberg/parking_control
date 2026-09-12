package com.aurora.parking.parkingspot.domain;


import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Vaga de estacionamento. Regra de negócio pura.
 * A ocupação (INV-02) é derivada de Movement — não duplicada aqui.
 */
public record ParkingSpot(
        UUID id,
        UUID apartmentId,
        String identifier,
        boolean enabled,
        Instant createdAt,
        Instant updatedAt
) {
    public ParkingSpot {
        Objects.requireNonNull(apartmentId, "apartmentId não pode ser nulo");
        Objects.requireNonNull(identifier, "identifier não pode ser nulo");
        if (identifier.isBlank()) {
            throw new IllegalArgumentException("identifier não pode ser vazio");
        }
    }

    public static ParkingSpot create(UUID id, UUID apartmentId, String identifier, Instant now) {
        return new ParkingSpot(id, apartmentId, identifier.trim().toUpperCase(), true, now, now);
    }

    public ParkingSpot reassign(UUID newApartmentId, Instant now) {
        return new ParkingSpot(id, newApartmentId, identifier, enabled, createdAt, now);
    }

    public ParkingSpot rename(String newIdentifier, Instant now) {
        return new ParkingSpot(id, apartmentId, newIdentifier.trim().toUpperCase(), enabled, createdAt, now);
    }

    public ParkingSpot disable(Instant now) {
        if (!enabled) {
            return this;
        }
        return new ParkingSpot(id, apartmentId, identifier, false, createdAt, now);
    }

    public ParkingSpot enable(Instant now) {
        if (enabled) {
            return this;
        }
        return new ParkingSpot(id, apartmentId, identifier, true, createdAt, now);
    }
}