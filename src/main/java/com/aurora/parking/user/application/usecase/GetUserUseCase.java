package com.aurora.parking.user.application.usecase;


import com.aurora.parking.execption.UserNotFoundException;
import com.aurora.parking.user.application.port.UserRepositoryPort;
import com.aurora.parking.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GetUserUseCase {

    private final UserRepositoryPort repository;

    public GetUserUseCase(UserRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public User execute(UUID id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }
}