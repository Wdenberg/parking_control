package com.aurora.parking.infrastructure.persistence.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
public class AuditLogJpaEntity {

    @Id
    private UUID id;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(name = "actor_user_id")
    private UUID actorUserId;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected AuditLogJpaEntity() {
    }

    public AuditLogJpaEntity(UUID id, String eventType, UUID actorUserId,
                             String details, Instant createdAt) {
        this.id = id;
        this.eventType = eventType;
        this.actorUserId = actorUserId;
        this.details = details;
        this.createdAt = createdAt;
    }
}
