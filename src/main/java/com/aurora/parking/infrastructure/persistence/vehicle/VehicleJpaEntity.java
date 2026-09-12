package com.aurora.parking.infrastructure.persistence.vehicle;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "vehicles",
        uniqueConstraints = @UniqueConstraint(name = "uq_vehicle_normalized_plate", columnNames = "plate")
)
public class VehicleJpaEntity {

    @Id
    private UUID id;

    @Column(name = "apartment_id", nullable = false)
    private UUID apartmentId;

    @Column(nullable = false, length = 8)
    private String plate;

    @Column(length = 60)
    private String model;

    @Column(length = 30)
    private String color;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected VehicleJpaEntity() {
        // JPA
    }

    public VehicleJpaEntity(UUID id, UUID apartmentId, String plate, String model, String color,
                            boolean active, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.apartmentId = apartmentId;
        this.plate = plate;
        this.model = model;
        this.color = color;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public UUID getApartmentId() { return apartmentId; }
    public String getPlate() { return plate; }
    public String getModel() { return model; }
    public String getColor() { return color; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
