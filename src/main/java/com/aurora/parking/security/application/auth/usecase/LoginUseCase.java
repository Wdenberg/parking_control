package com.aurora.parking.security.application.auth.usecase;


import com.aurora.parking.audit.application.port.AuditLogPort;
import com.aurora.parking.execption.InvalidCredentialsException;
import com.aurora.parking.execption.UserDisabledException;
import com.aurora.parking.security.application.JwtTokenPort;
import com.aurora.parking.security.application.PasswordEncoderPort;
import com.aurora.parking.security.application.auth.dto.TokenPairResponse;
import com.aurora.parking.security.application.auth.port.RefreshTokenRepositoryPort;
import com.aurora.parking.user.domain.User;
import com.aurora.parking.user.application.port.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

@Service
public class LoginUseCase {

    private static final Duration REFRESH_TTL = Duration.ofDays(7);
    private static final long ACCESS_TTL_SECONDS = Duration.ofMinutes(15).toSeconds();

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final JwtTokenPort jwtTokenPort;
    private final RefreshTokenRepositoryPort refreshTokenRepository;
    private final AuditLogPort auditLogPort;
    private final Clock clock;

    public LoginUseCase(UserRepositoryPort userRepository,
                        PasswordEncoderPort passwordEncoder,
                        JwtTokenPort jwtTokenPort,
                        RefreshTokenRepositoryPort refreshTokenRepository,
                        AuditLogPort auditLogPort,
                        Clock clock) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenPort = jwtTokenPort;
        this.refreshTokenRepository = refreshTokenRepository;
        this.auditLogPort = auditLogPort;
        this.clock = clock;
    }

    /**
     * REQUIRES_NEW: login deve ser auditado mesmo que algo falhe depois
     * (AGENT.md §33 — AuditLogService usa REQUIRES_NEW para login).
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TokenPairResponse execute(String username, String rawPassword) {
        var userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty() || !passwordEncoder.matches(rawPassword, userOpt.get().passwordHash())) {
            auditLogPort.record("LOGIN_FAILED", null, "username=" + username);
            throw new InvalidCredentialsException();
        }

        User user = userOpt.get();

        if (!user.active()) {
            auditLogPort.record("LOGIN_FAILED", user.id(), "usuário desabilitado");
            throw new UserDisabledException(username);
        }

        String accessToken = jwtTokenPort.generateAccessToken(user.id(), user.username(), user.role().name());
        String rawRefreshToken = generateRawRefreshToken();
        String refreshHash = sha256(rawRefreshToken);

        Instant now = Instant.now(clock);
        refreshTokenRepository.issue(user.id(), refreshHash, now.plus(REFRESH_TTL));

        auditLogPort.record("LOGIN", user.id(), null);

        return new TokenPairResponse(accessToken, rawRefreshToken, ACCESS_TTL_SECONDS);
    }

    private String generateRawRefreshToken() {
        byte[] bytes = new byte[64];
        new java.security.SecureRandom().nextBytes(bytes);
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
