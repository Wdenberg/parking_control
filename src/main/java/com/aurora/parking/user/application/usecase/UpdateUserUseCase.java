package com.aurora.parking.user.application.usecase;


import com.aurora.parking.execption.UserNotFoundException;
import com.aurora.parking.user.application.port.UserRepositoryPort;
import com.aurora.parking.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class UpdateUserUseCase {

    private final UserRepositoryPort repository;
    private final Clock clock;

    public UpdateUserUseCase(UserRepositoryPort repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Transactional
    public User execute(UUID id, String fullName, String email) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        Instant now = Instant.now(clock);
        return repository.save(user.updateDetails(fullName, email, now));
    }
}