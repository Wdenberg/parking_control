package com.aurora.parking.parkingspot.application.parkingspot.usecase;

import com.aurora.parking.apartment.application.port.ApartmentRepositoryPort;
import com.aurora.parking.execption.ApartmentNotFoundException;
import com.aurora.parking.execption.DuplicateParkingSpotException;
import com.aurora.parking.parkingspot.application.parkingspot.port.ParkingSpotRepositoryPort;
import com.aurora.parking.parkingspot.domain.ParkingSpot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class CreateParkingSpotUseCase {

    private final ParkingSpotRepositoryPort parkingSpotRepository;
    private final ApartmentRepositoryPort apartmentRepository;
    private final Clock clock;

    public CreateParkingSpotUseCase(ParkingSpotRepositoryPort parkingSpotRepository,
                                    ApartmentRepositoryPort apartmentRepository,
                                    Clock clock) {
        this.parkingSpotRepository = parkingSpotRepository;
        this.apartmentRepository = apartmentRepository;
        this.clock = clock;
    }

    @Transactional
    public ParkingSpot execute(UUID apartmentId, String identifier) {
        apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new ApartmentNotFoundException(apartmentId));

        if (parkingSpotRepository.existsByIdentifier(identifier)) {
            throw new DuplicateParkingSpotException(identifier);
        }

        Instant now = Instant.now(clock);
        ParkingSpot spot = ParkingSpot.create(UUID.randomUUID(), apartmentId, identifier, now);
        return parkingSpotRepository.save(spot);
    }
}