package com.aurora.parking.security.application.auth.port;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepositoryPort {

    /** Persiste um novo refresh token (hash, não o valor em claro). */
    UUID issue(UUID userId, String tokenHash, Instant expiresAt);

    Optional<StoredRefreshToken> findActiveByHash(String tokenHash);

    /** Rotação: marca o antigo como usado/revogado, vinculando ao novo. */
    void revokeAndReplace(UUID oldTokenId, UUID newTokenId);

    /** Logout: revoga sem substituir. */
    void revoke(UUID tokenId);

    /** Revoga todos os refresh tokens ativos de um usuário (ex: reset de senha). */
    void revokeAllForUser(UUID userId);

    record StoredRefreshToken(UUID id, UUID userId, Instant expiresAt, boolean revoked) {}
}