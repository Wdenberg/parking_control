package com.aurora.parking.vehicle.application.vehicle.usecase;

import com.aurora.parking.vehicle.application.vehicle.port.VehicleRepositoryPort;
import com.aurora.parking.vehicle.domain.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ListVehiclesUseCase {

    private final VehicleRepositoryPort repository;

    public ListVehiclesUseCase(VehicleRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Vehicle> execute(Pageable pageable, UUID apartmentIdFilter, Boolean activeFilter) {
        return repository.findAll(pageable, apartmentIdFilter, activeFilter);
    }
}