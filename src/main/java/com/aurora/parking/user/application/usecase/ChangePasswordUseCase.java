package com.aurora.parking.user.application.usecase;


import com.aurora.parking.execption.UserNotFoundException;
import com.aurora.parking.security.application.PasswordEncoderPort;
import com.aurora.parking.user.application.port.UserRepositoryPort;
import com.aurora.parking.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

/**
 * Troca de senha pelo próprio usuário ou por admin (via ADMIN/SUPER_ADMIN).
 * A validação de "quem pode chamar isso" fica no controller (RBAC),
 * não neste use case.
 */
@Service
public class ChangePasswordUseCase {

    private final UserRepositoryPort repository;
    private final PasswordEncoderPort passwordEncoder;
    private final Clock clock;

    public ChangePasswordUseCase(UserRepositoryPort repository, PasswordEncoderPort passwordEncoder, Clock clock) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.clock = clock;
    }

    @Transactional
    public void execute(UUID id, String newRawPassword) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        String newHash = passwordEncoder.encode(newRawPassword);
        Instant now = Instant.now(clock);
        repository.save(user.changePasswordHash(newHash, now));
    }
}