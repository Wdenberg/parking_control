package com.aurora.parking.user.application.usecase;

import com.aurora.parking.execption.UserNotFoundException;
import com.aurora.parking.role.domain.RoleCode;
import com.aurora.parking.security.application.auth.port.RefreshTokenRepositoryPort;
import com.aurora.parking.user.application.port.UserRepositoryPort;
import com.aurora.parking.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class ChangeUserRoleUseCase {

    private final UserRepositoryPort repository;
    private final RefreshTokenRepositoryPort refreshTokenRepository;
    private final Clock clock;

    public ChangeUserRoleUseCase(UserRepositoryPort repository,
                                 RefreshTokenRepositoryPort refreshTokenRepository,
                                 Clock clock) {
        this.repository = repository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.clock = clock;
    }

    @Transactional
    public User execute(UUID id, RoleCode newRole) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        Instant now = Instant.now(clock);
        User updated = repository.save(user.changeRole(newRole, now));

        // Mudança de role invalida sessões ativas — evita que o usuário
        // continue operando com permissions antigas via access token já emitido
        // ou via refresh token existente.
        refreshTokenRepository.revokeAllForUser(id);

        return updated;
    }
}