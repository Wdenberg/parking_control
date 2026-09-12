package com.aurora.parking.infrastructure.persistence.audit;

import com.aurora.parking.audit.application.port.AuditLogPort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

/**
 * Temporary infrastructure adapter until the full audit query/reporting
 * surface is implemented. It keeps authentication wiring explicit.
 */
@Component
public class AuditLogAdapter implements AuditLogPort {

    private final SpringDataAuditLogJpaRepository repository;

    public AuditLogAdapter(SpringDataAuditLogJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void record(String eventType, UUID actorUserId, String details) {
        repository.save(new AuditLogJpaEntity(
                UUID.randomUUID(), eventType, actorUserId, details, Instant.now()));
    }
}
