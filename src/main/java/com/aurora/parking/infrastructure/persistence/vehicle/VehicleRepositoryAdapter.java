package com.aurora.parking.infrastructure.persistence.vehicle;

import com.aurora.parking.vehicle.application.vehicle.port.VehicleRepositoryPort;
import com.aurora.parking.vehicle.domain.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class VehicleRepositoryAdapter implements VehicleRepositoryPort {

    private final SpringDataVehicleJpaRepository jpaRepository;

    public VehicleRepositoryAdapter(SpringDataVehicleJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return toDomain(jpaRepository.save(toEntity(vehicle)));
    }

    @Override
    public Optional<Vehicle> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Vehicle> findByPlate(String normalizedPlate) {
        return jpaRepository.findByPlate(normalizedPlate).map(this::toDomain);
    }

    @Override
    public boolean existsByPlate(String normalizedPlate) {
        return jpaRepository.existsByPlate(normalizedPlate);
    }

    @Override
    public boolean existsByPlateAndIdNot(String normalizedPlate, UUID excludingId) {
        return jpaRepository.existsByPlateAndIdNot(normalizedPlate, excludingId);
    }

    @Override
    public boolean isCurrentlyParked(UUID vehicleId) {
        return jpaRepository.existsOpenMovementForVehicle(vehicleId);
    }

    @Override
    public Page<Vehicle> findAll(Pageable pageable, UUID apartmentIdFilter, Boolean activeFilter) {
        Page<VehicleJpaEntity> page;
        if (apartmentIdFilter != null && activeFilter != null) {
            page = jpaRepository.findAllByApartmentIdAndActive(apartmentIdFilter, activeFilter, pageable);
        } else if (apartmentIdFilter != null) {
            page = jpaRepository.findAllByApartmentId(apartmentIdFilter, pageable);
        } else if (activeFilter != null) {
            page = jpaRepository.findAllByActive(activeFilter, pageable);
        } else {
            page = jpaRepository.findAll(pageable);
        }
        return page.map(this::toDomain);
    }

    private VehicleJpaEntity toEntity(Vehicle vehicle) {
        return new VehicleJpaEntity(
                vehicle.id(), vehicle.apartmentId(), vehicle.plate(), vehicle.model(),
                vehicle.color(), vehicle.active(), vehicle.createdAt(), vehicle.updatedAt()
        );
    }

    private Vehicle toDomain(VehicleJpaEntity entity) {
        return new Vehicle(
                entity.getId(), entity.getApartmentId(), entity.getPlate(), entity.getModel(),
                entity.getColor(), entity.isActive(), entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }
}