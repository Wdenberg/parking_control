package com.aurora.parking.security.application.auth.usecase;


import com.aurora.parking.execption.InvalidCredentialsException;
import com.aurora.parking.execption.UserDisabledException;
import com.aurora.parking.security.application.JwtTokenPort;
import com.aurora.parking.security.application.auth.dto.TokenPairResponse;
import com.aurora.parking.security.application.auth.port.RefreshTokenRepositoryPort;
import com.aurora.parking.user.application.port.UserRepositoryPort;
import com.aurora.parking.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

/**
 * Rotação de refresh token: o token usado é imediatamente revogado
 * e substituído por um novo. Reutilizar um refresh já usado invalida
 * a sessão inteira (indício de token roubado) — comportamento padrão
 * de rotação segura.
 */
@Service
public class RefreshTokenUseCase {

    private static final Duration REFRESH_TTL = Duration.ofDays(7);
    private static final long ACCESS_TTL_SECONDS = Duration.ofMinutes(15).toSeconds();

    private final RefreshTokenRepositoryPort refreshTokenRepository;
    private final UserRepositoryPort userRepository;
    private final JwtTokenPort jwtTokenPort;
    private final Clock clock;

    public RefreshTokenUseCase(RefreshTokenRepositoryPort refreshTokenRepository,
                               UserRepositoryPort userRepository,
                               JwtTokenPort jwtTokenPort,
                               Clock clock) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.jwtTokenPort = jwtTokenPort;
        this.clock = clock;
    }

    @Transactional
    public TokenPairResponse execute(String rawRefreshToken) {
        String hash = sha256(rawRefreshToken);

        var stored = refreshTokenRepository.findActiveByHash(hash)
                .orElseThrow(InvalidCredentialsException::new);

        if (stored.revoked() || stored.expiresAt().isBefore(Instant.now(clock))) {
            throw new InvalidCredentialsException();
        }

        User user = userRepository.findById(stored.userId())
                .orElseThrow(InvalidCredentialsException::new);

        if (!user.active()) {
            throw new UserDisabledException(user.username());
        }

        String newRawRefreshToken = generateRawRefreshToken();
        String newHash = sha256(newRawRefreshToken);
        Instant now = Instant.now(clock);

        UUID newTokenId = refreshTokenRepository.issue(user.id(), newHash, now.plus(REFRESH_TTL));
        refreshTokenRepository.revokeAndReplace(stored.id(), newTokenId);

        String accessToken = jwtTokenPort.generateAccessToken(user.id(), user.username(), user.role().name());

        return new TokenPairResponse(accessToken, newRawRefreshToken, ACCESS_TTL_SECONDS);
    }

    private String generateRawRefreshToken() {
        byte[] bytes = new byte[64];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
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