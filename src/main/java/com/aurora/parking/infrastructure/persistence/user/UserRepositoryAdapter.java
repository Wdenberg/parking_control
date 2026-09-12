package com.aurora.parking.infrastructure.persistence.user;


import com.aurora.parking.role.domain.RoleCode;
import com.aurora.parking.user.application.port.UserRepositoryPort;
import com.aurora.parking.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final SpringDataUserJpaRepository jpaRepository;

    public UserRepositoryAdapter(SpringDataUserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User save(User user) {
        return toDomain(jpaRepository.save(toEntity(user)));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username).map(this::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByUsernameAndIdNot(String username, UUID excludingId) {
        return jpaRepository.existsByUsernameAndIdNot(username, excludingId);
    }

    @Override
    public Page<User> findAll(Pageable pageable, RoleCode roleFilter, Boolean activeFilter) {
        Page<UserJpaEntity> page;
        if (roleFilter != null && activeFilter != null) {
            page = jpaRepository.findAllByRoleAndActive(roleFilter, activeFilter, pageable);
        } else if (roleFilter != null) {
            page = jpaRepository.findAllByRole(roleFilter, pageable);
        } else if (activeFilter != null) {
            page = jpaRepository.findAllByActive(activeFilter, pageable);
        } else {
            page = jpaRepository.findAll(pageable);
        }
        return page.map(this::toDomain);
    }

    private UserJpaEntity toEntity(User user) {
        return new UserJpaEntity(
                user.id(), user.username(), user.passwordHash(), user.fullName(), user.email(),
                user.role(), user.active(), user.createdAt(), user.updatedAt()
        );
    }

    private User toDomain(UserJpaEntity entity) {
        return new User(
                entity.getId(), entity.getUsername(), entity.getPasswordHash(), entity.getFullName(),
                entity.getEmail(), entity.getRole(), entity.isActive(), entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }
}