package com.aurora.parking.infrastructure.persistence.movement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity(name = "MovementJpaEntity")
@Table(name = "movements")
public class MovementJpaEntity {

    @Id
    private UUID id;

    @Column(name = "vehicle_id", nullable = false)
    private UUID vehicleId;

    @Column(name = "parking_spot_id", nullable = false)
    private UUID parkingSpotId;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "entry_at", nullable = false)
    private Instant entryAt;

    @Column(name = "exit_at")
    private Instant exitAt;

    protected MovementJpaEntity() {
    }
}
