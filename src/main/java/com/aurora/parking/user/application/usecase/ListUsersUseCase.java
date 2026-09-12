package com.aurora.parking.user.application.usecase;


import com.aurora.parking.role.domain.RoleCode;
import com.aurora.parking.user.application.port.UserRepositoryPort;
import com.aurora.parking.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListUsersUseCase {

    private final UserRepositoryPort repository;

    public ListUsersUseCase(UserRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<User> execute(Pageable pageable, RoleCode roleFilter, Boolean activeFilter) {
        return repository.findAll(pageable, roleFilter, activeFilter);
    }
}