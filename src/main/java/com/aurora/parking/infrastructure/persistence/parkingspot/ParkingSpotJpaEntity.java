package com.aurora.parking.infrastructure.persistence.parkingspot;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "parking_spots",
        uniqueConstraints = @UniqueConstraint(name = "uq_parking_spot_identifier", columnNames = "identifier")
)
public class ParkingSpotJpaEntity {

    @Id
    private UUID id;

    @Column(name = "apartment_id", nullable = false)
    private UUID apartmentId;

    @Column(nullable = false, length = 10)
    private String identifier;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected ParkingSpotJpaEntity() {
        // JPA
    }

    public ParkingSpotJpaEntity(UUID id, UUID apartmentId, String identifier,
                                boolean enabled, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.apartmentId = apartmentId;
        this.identifier = identifier;
        this.enabled = enabled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public UUID getApartmentId() { return apartmentId; }
    public String getIdentifier() { return identifier; }
    public boolean isEnabled() { return enabled; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}