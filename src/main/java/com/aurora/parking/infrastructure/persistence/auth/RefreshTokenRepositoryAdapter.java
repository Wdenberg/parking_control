package com.aurora.parking.infrastructure.persistence.auth;


import com.aurora.parking.security.application.auth.port.RefreshTokenRepositoryPort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepositoryPort {

    private final SpringDataRefreshTokenJpaRepository jpaRepository;

    public RefreshTokenRepositoryAdapter(SpringDataRefreshTokenJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public UUID issue(UUID userId, String tokenHash, Instant expiresAt) {
        UUID id = UUID.randomUUID();
        RefreshTokenJpaEntity entity = new RefreshTokenJpaEntity(
                id, userId, tokenHash, Instant.now(), expiresAt, false, null);
        jpaRepository.save(entity);
        return id;
    }

    @Override
    public Optional<StoredRefreshToken> findActiveByHash(String tokenHash) {
        return jpaRepository.findByTokenHash(tokenHash)
                .map(e -> new StoredRefreshToken(e.getId(), e.getUserId(), e.getExpiresAt(), e.isRevoked()));
    }

    @Override
    public void revokeAndReplace(UUID oldTokenId, UUID newTokenId) {
        jpaRepository.findById(oldTokenId).ifPresent(entity -> {
            entity.setRevoked(true);
            entity.setReplacedById(newTokenId);
            jpaRepository.save(entity);
        });
    }

    @Override
    public void revoke(UUID tokenId) {
        jpaRepository.findById(tokenId).ifPresent(entity -> {
            entity.setRevoked(true);
            jpaRepository.save(entity);
        });
    }

    @Override
    public void revokeAllForUser(UUID userId) {
        jpaRepository.revokeAllActiveForUser(userId);
    }
}