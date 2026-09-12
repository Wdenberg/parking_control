package com.aurora.parking.infrastructure.persistence.parkingspot;


import com.aurora.parking.parkingspot.application.parkingspot.port.ParkingSpotRepositoryPort;
import com.aurora.parking.parkingspot.domain.ParkingSpot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ParkingSpotRepositoryAdapter implements ParkingSpotRepositoryPort {

    private final SpringDataParkingSpotJpaRepository jpaRepository;

    public ParkingSpotRepositoryAdapter(SpringDataParkingSpotJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ParkingSpot save(ParkingSpot parkingSpot) {
        return toDomain(jpaRepository.save(toEntity(parkingSpot)));
    }

    @Override
    public Optional<ParkingSpot> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsByIdentifier(String identifier) {
        return jpaRepository.existsByIdentifier(identifier);
    }

    @Override
    public boolean existsByIdentifierAndIdNot(String identifier, UUID excludingId) {
        return jpaRepository.existsByIdentifierAndIdNot(identifier, excludingId);
    }

    @Override
    public boolean isCurrentlyOccupied(UUID parkingSpotId) {
        return jpaRepository.existsOpenMovementForSpot(parkingSpotId);
    }

    @Override
    public Page<ParkingSpot> findAll(Pageable pageable, UUID apartmentIdFilter, Boolean enabledFilter) {
        Page<ParkingSpotJpaEntity> page;
        if (apartmentIdFilter != null && enabledFilter != null) {
            page = jpaRepository.findAllByApartmentIdAndEnabled(apartmentIdFilter, enabledFilter, pageable);
        } else if (apartmentIdFilter != null) {
            page = jpaRepository.findAllByApartmentId(apartmentIdFilter, pageable);
        } else if (enabledFilter != null) {
            page = jpaRepository.findAllByEnabled(enabledFilter, pageable);
        } else {
            page = jpaRepository.findAll(pageable);
        }
        return page.map(this::toDomain);
    }

    private ParkingSpotJpaEntity toEntity(ParkingSpot spot) {
        return new ParkingSpotJpaEntity(
                spot.id(), spot.apartmentId(), spot.identifier(),
                spot.enabled(), spot.createdAt(), spot.updatedAt()
        );
    }

    private ParkingSpot toDomain(ParkingSpotJpaEntity entity) {
        return new ParkingSpot(
                entity.getId(), entity.getApartmentId(), entity.getIdentifier(),
                entity.isEnabled(), entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }
}