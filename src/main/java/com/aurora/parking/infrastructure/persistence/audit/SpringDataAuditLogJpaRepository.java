package com.aurora.parking.infrastructure.persistence.audit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataAuditLogJpaRepository extends JpaRepository<AuditLogJpaEntity, UUID> {
}
