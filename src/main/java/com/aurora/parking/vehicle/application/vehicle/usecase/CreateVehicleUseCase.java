package com.aurora.parking.vehicle.application.vehicle.usecase;

import com.aurora.parking.apartment.application.port.ApartmentRepositoryPort;
import com.aurora.parking.execption.ApartmentNotFoundException;
import com.aurora.parking.execption.DuplicatePlateException;
import com.aurora.parking.vehicle.application.vehicle.port.VehicleRepositoryPort;
import com.aurora.parking.vehicle.domain.PlateNormalizer;
import com.aurora.parking.vehicle.domain.Vehicle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class CreateVehicleUseCase {

    private final VehicleRepositoryPort vehicleRepository;
    private final ApartmentRepositoryPort apartmentRepository;
    private final Clock clock;

    public CreateVehicleUseCase(VehicleRepositoryPort vehicleRepository,
                                ApartmentRepositoryPort apartmentRepository,
                                Clock clock) {
        this.vehicleRepository = vehicleRepository;
        this.apartmentRepository = apartmentRepository;
        this.clock = clock;
    }

    @Transactional
    public Vehicle execute(UUID apartmentId, String rawPlate, String model, String color) {
        apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new ApartmentNotFoundException(apartmentId));

        String normalizedPlate = PlateNormalizer.normalize(rawPlate);

        if (vehicleRepository.existsByPlate(normalizedPlate)) {
            throw new DuplicatePlateException(normalizedPlate);
        }

        Instant now = Instant.now(clock);
        Vehicle vehicle = Vehicle.create(UUID.randomUUID(), apartmentId, normalizedPlate, model, color, now);
        return vehicleRepository.save(vehicle);
    }
}