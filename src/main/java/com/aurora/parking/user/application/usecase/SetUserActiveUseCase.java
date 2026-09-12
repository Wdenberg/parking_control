package com.aurora.parking.user.application.usecase;


import com.aurora.parking.execption.UserNotFoundException;
import com.aurora.parking.security.application.auth.port.RefreshTokenRepositoryPort;
import com.aurora.parking.user.application.port.UserRepositoryPort;
import com.aurora.parking.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class SetUserActiveUseCase {

    private final UserRepositoryPort repository;
    private final RefreshTokenRepositoryPort refreshTokenRepository;
    private final Clock clock;

    public SetUserActiveUseCase(UserRepositoryPort repository,
                                RefreshTokenRepositoryPort refreshTokenRepository,
                                Clock clock) {
        this.repository = repository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.clock = clock;
    }

    @Transactional
    public User execute(UUID id, boolean active) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        Instant now = Instant.now(clock);
        User updated = repository.save(active ? user.activate(now) : user.deactivate(now));

        if (!active) {
            refreshTokenRepository.revokeAllForUser(id);
        }
        return updated;
    }
}