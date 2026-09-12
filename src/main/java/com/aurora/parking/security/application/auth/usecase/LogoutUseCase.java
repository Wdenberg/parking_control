package com.aurora.parking.security.application.auth.usecase;

import com.aurora.parking.security.application.auth.port.RefreshTokenRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class LogoutUseCase {

    private final RefreshTokenRepositoryPort refreshTokenRepository;

    public LogoutUseCase(RefreshTokenRepositoryPort refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public void execute(String rawRefreshToken) {
        String hash = sha256(rawRefreshToken);
        refreshTokenRepository.findActiveByHash(hash)
                .ifPresent(stored -> refreshTokenRepository.revoke(stored.id()));
        // Idempotente de propósito: logout de token já inválido não é erro.
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 não disponível", e);
        }
    }
}