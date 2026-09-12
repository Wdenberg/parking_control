package com.aurora.parking.user.application.port;


import com.aurora.parking.role.domain.RoleCode;
import com.aurora.parking.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findById(UUID id);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, UUID excludingId);

    Page<User> findAll(Pageable pageable, RoleCode roleFilter, Boolean activeFilter);
}