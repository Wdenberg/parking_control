package com.aurora.parking.infrastructure.persistence.camera;

import com.aurora.parking.camera.domain.CameraOperation;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "cameras",
        uniqueConstraints = @UniqueConstraint(name = "uq_camera_code", columnNames = "code")
)
public class CameraJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 20)
    private String code;

    @Column(length = 100)
    private String host;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private CameraOperation operation;

    @Column(length = 200)
    private String description;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected CameraJpaEntity() {
        // JPA
    }

    public CameraJpaEntity(UUID id, String code, String host, CameraOperation operation,
                           String description, boolean enabled, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.code = code;
        this.host = host;
        this.operation = operation;
        this.description = description;
        this.enabled = enabled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public String getCode() { return code; }
    public String getHost() { return host; }
    public CameraOperation getOperation() { return operation; }
    public String getDescription() { return description; }
    public boolean isEnabled() { return enabled; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}