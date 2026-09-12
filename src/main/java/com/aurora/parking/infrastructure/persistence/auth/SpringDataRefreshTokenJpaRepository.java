package com.aurora.parking.infrastructure.persistence.auth;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataRefreshTokenJpaRepository extends JpaRepository<RefreshTokenJpaEntity, UUID> {

    Optional<RefreshTokenJpaEntity> findByTokenHash(String tokenHash);

    List<RefreshTokenJpaEntity> findAllByUserIdAndRevokedFalse(UUID userId);

    @Modifying
    @Query("UPDATE RefreshTokenJpaEntity t SET t.revoked = true WHERE t.userId = :userId AND t.revoked = false")
    void revokeAllActiveForUser(@Param("userId") UUID userId);
}