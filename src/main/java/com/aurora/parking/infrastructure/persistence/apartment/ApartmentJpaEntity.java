package com.aurora.parking.infrastructure.persistence.apartment;


import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "apartments",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_apartment_block_number",
                columnNames = {"block", "number"}
        )
)
public class ApartmentJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 10)
    private String block;

    @Column(nullable = false, length = 10)
    private String number;

    @Column(nullable = false, length = 32)
    private String identifier;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected ApartmentJpaEntity() {
        // JPA
    }

    public ApartmentJpaEntity(UUID id, String block, String number, String identifier,
                              boolean active, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.block = block;
        this.number = number;
        this.identifier = identifier;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // getters (sem setters públicos — entidade JPA é mero mapeamento)
    public UUID getId() { return id; }
    public String getBlock() { return block; }
    public String getNumber() { return number; }
    public String getIdentifier() { return identifier; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}