package com.aurora.parking.vehicle.application.vehicle.port;


import com.aurora.parking.vehicle.domain.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface VehicleRepositoryPort {

    Vehicle save(Vehicle vehicle);

    Optional<Vehicle> findById(UUID id);

    Optional<Vehicle> findByPlate(String normalizedPlate);

    boolean existsByPlate(String normalizedPlate);

    boolean existsByPlateAndIdNot(String normalizedPlate, UUID excludingId);

    /** true se existir Movement OPEN para este veículo (INV-01). */
    boolean isCurrentlyParked(UUID vehicleId);

    Page<Vehicle> findAll(Pageable pageable, UUID apartmentIdFilter, Boolean activeFilter);
}