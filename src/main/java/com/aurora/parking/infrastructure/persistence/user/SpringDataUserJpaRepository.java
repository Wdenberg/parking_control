package com.aurora.parking.infrastructure.persistence.user;


import com.aurora.parking.role.domain.RoleCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataUserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {

    Optional<UserJpaEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, UUID id);

    Page<UserJpaEntity> findAllByRole(RoleCode role, Pageable pageable);

    Page<UserJpaEntity> findAllByActive(boolean active, Pageable pageable);

    Page<UserJpaEntity> findAllByRoleAndActive(RoleCode role, boolean active, Pageable pageable);
}