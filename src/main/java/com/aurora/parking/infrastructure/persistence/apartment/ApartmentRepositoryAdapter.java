package com.aurora.parking.infrastructure.persistence.apartment;

import com.aurora.parking.apartment.application.port.ApartmentRepositoryPort;
import com.aurora.parking.apartment.domain.Apartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ApartmentRepositoryAdapter implements ApartmentRepositoryPort {

    private final SpringDataApartmentJpaRepository jpaRepository;

    public ApartmentRepositoryAdapter(SpringDataApartmentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Apartment save(Apartment apartment) {
        ApartmentJpaEntity entity = toEntity(apartment);
        ApartmentJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Apartment> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsByBlockAndNumber(String block, String number) {
        return jpaRepository.existsByBlockAndNumber(block, number);
    }

    @Override
    public boolean existsByBlockAndNumberAndIdNot(String block, String number, UUID excludingId) {
        return jpaRepository.existsByBlockAndNumberAndIdNot(block, number, excludingId);
    }



    @Override
    public Page<Apartment> findAll(Pageable pageable, Boolean activeFilter) {
        Page<ApartmentJpaEntity> page = (activeFilter == null)
                ? jpaRepository.findAll(pageable)
                : jpaRepository.findAllByActive(activeFilter, pageable);
        return page.map(this::toDomain);
    }



    private ApartmentJpaEntity toEntity(Apartment apartment) {
        return new ApartmentJpaEntity(
                apartment.id(),
                apartment.block(),
                apartment.number(),
                apartment.identifier(),
                apartment.active(),
                apartment.createdAt(),
                apartment.updatedAt()
        );
    }

    private Apartment toDomain(ApartmentJpaEntity entity) {
        return new Apartment(
                entity.getId(),
                entity.getBlock(),
                entity.getNumber(),
                entity.getIdentifier(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}