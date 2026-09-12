package com.aurora.parking.user.application.usecase;


import com.aurora.parking.execption.DuplicateUsernameException;
import com.aurora.parking.role.domain.RoleCode;
import com.aurora.parking.security.application.PasswordEncoderPort;
import com.aurora.parking.user.application.port.UserRepositoryPort;
import com.aurora.parking.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class CreateUserUseCase {

    private final UserRepositoryPort repository;
    private final PasswordEncoderPort passwordEncoder;
    private final Clock clock;

    public CreateUserUseCase(UserRepositoryPort repository, PasswordEncoderPort passwordEncoder, Clock clock) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.clock = clock;
    }

    @Transactional
    public User execute(String username, String rawPassword, String fullName, String email, RoleCode role) {
        if (repository.existsByUsername(username)) {
            throw new DuplicateUsernameException(username);
        }
        String hash = passwordEncoder.encode(rawPassword);
        Instant now = Instant.now(clock);
        User user = User.create(UUID.randomUUID(), username, hash, fullName, email, role, now);
        return repository.save(user);
    }
}