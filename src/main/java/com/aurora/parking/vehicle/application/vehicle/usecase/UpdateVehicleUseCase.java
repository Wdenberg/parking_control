package com.aurora.parking.vehicle.application.vehicle.usecase;


import com.aurora.parking.apartment.application.port.ApartmentRepositoryPort;
import com.aurora.parking.execption.ApartmentNotFoundException;
import com.aurora.parking.execption.DuplicatePlateException;
import com.aurora.parking.execption.VehicleCurrentlyParkedException;
import com.aurora.parking.execption.VehicleNotFoundException;
import com.aurora.parking.vehicle.application.vehicle.port.VehicleRepositoryPort;
import com.aurora.parking.vehicle.domain.PlateNormalizer;
import com.aurora.parking.vehicle.domain.Vehicle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class UpdateVehicleUseCase {

    private final VehicleRepositoryPort vehicleRepository;
    private final ApartmentRepositoryPort apartmentRepository;
    private final Clock clock;

    public UpdateVehicleUseCase(VehicleRepositoryPort vehicleRepository,
                                ApartmentRepositoryPort apartmentRepository,
                                Clock clock) {
        this.vehicleRepository = vehicleRepository;
        this.apartmentRepository = apartmentRepository;
        this.clock = clock;
    }

    @Transactional
    public Vehicle execute(UUID id, UUID newApartmentId, String rawPlate, String model, String color) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));

        String normalizedPlate = PlateNormalizer.normalize(rawPlate);

        boolean plateChanged = !vehicle.plate().equals(normalizedPlate);
        boolean apartmentChanged = !vehicle.apartmentId().equals(newApartmentId);

        if ((plateChanged || apartmentChanged) && vehicleRepository.isCurrentlyParked(id)) {
            throw new VehicleCurrentlyParkedException(id);
        }

        if (plateChanged && vehicleRepository.existsByPlateAndIdNot(normalizedPlate, id)) {
            throw new DuplicatePlateException(normalizedPlate);
        }

        if (apartmentChanged) {
            apartmentRepository.findById(newApartmentId)
                    .orElseThrow(() -> new ApartmentNotFoundException(newApartmentId));
        }

        Instant now = Instant.now(clock);
        Vehicle updated = vehicle
                .reassign(newApartmentId, now)
                .updateDetails(model, color, now);
        // plate não muda via reassign/updateDetails — reconstrução explícita se plateChanged
        if (plateChanged) {
            updated = new Vehicle(updated.id(), updated.apartmentId(), normalizedPlate,
                    updated.model(), updated.color(), updated.active(), updated.createdAt(), now);
        }
        return vehicleRepository.save(updated);
    }
}