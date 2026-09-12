package com.aurora.parking.audit.application.port;

import java.util.UUID;

/**
 * STATUS: NÃO CONFIRMADO — assumo que já existe um AuditLogService
 * real da Fase 6 (auditoria de Movement). Esta porta é a interface
 * mínima que este módulo precisa; se já existir uma abstração
 * equivalente, esta deve ser removida e substituída pela real.
 */
public interface AuditLogPort {
    void record(String eventType, UUID actorUserId, String details);
}