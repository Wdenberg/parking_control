package com.aurora.parking.vehicle.application.vehicle.usecase;

import com.aurora.parking.execption.VehicleCurrentlyParkedException;
import com.aurora.parking.execption.VehicleNotFoundException;
import com.aurora.parking.vehicle.application.vehicle.port.VehicleRepositoryPort;
import com.aurora.parking.vehicle.domain.Vehicle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class SetVehicleActiveUseCase {

    private final VehicleRepositoryPort repository;
    private final Clock clock;

    public SetVehicleActiveUseCase(VehicleRepositoryPort repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Transactional
    public Vehicle execute(UUID id, boolean active) {
        Vehicle vehicle = repository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));

        if (!active && repository.isCurrentlyParked(id)) {
            throw new VehicleCurrentlyParkedException(id);
        }

        Instant now = Instant.now(clock);
        Vehicle updated = active ? vehicle.activate(now) : vehicle.deactivate(now);
        return repository.save(updated);
    }
}
