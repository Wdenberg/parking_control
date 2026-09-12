package com.aurora.parking.parkingspot.application.parkingspot.usecase;


import com.aurora.parking.apartment.application.port.ApartmentRepositoryPort;
import com.aurora.parking.execption.ApartmentNotFoundException;
import com.aurora.parking.execption.DuplicateParkingSpotException;
import com.aurora.parking.execption.ParkingSpotNotFoundException;
import com.aurora.parking.execption.ParkingSpotOccupiedException;
import com.aurora.parking.parkingspot.application.parkingspot.port.ParkingSpotRepositoryPort;
import com.aurora.parking.parkingspot.domain.ParkingSpot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class UpdateParkingSpotUseCase {

    private final ParkingSpotRepositoryPort parkingSpotRepository;
    private final ApartmentRepositoryPort apartmentRepository;
    private final Clock clock;

    public UpdateParkingSpotUseCase(ParkingSpotRepositoryPort parkingSpotRepository,
                                    ApartmentRepositoryPort apartmentRepository,
                                    Clock clock) {
        this.parkingSpotRepository = parkingSpotRepository;
        this.apartmentRepository = apartmentRepository;
        this.clock = clock;
    }

    @Transactional
    public ParkingSpot execute(UUID id, UUID newApartmentId, String newIdentifier) {
        ParkingSpot spot = parkingSpotRepository.findById(id)
                .orElseThrow(() -> new ParkingSpotNotFoundException(id));

        // INV-03: vaga pertence ao apartamento — reatribuição só é segura sem ocupação ativa
        if (!spot.apartmentId().equals(newApartmentId)
                && parkingSpotRepository.isCurrentlyOccupied(id)) {
            throw new ParkingSpotOccupiedException(id);
        }

        apartmentRepository.findById(newApartmentId)
                .orElseThrow(() -> new ApartmentNotFoundException(newApartmentId));

        if (!spot.identifier().equalsIgnoreCase(newIdentifier)
                && parkingSpotRepository.existsByIdentifierAndIdNot(newIdentifier, id)) {
            throw new DuplicateParkingSpotException(newIdentifier);
        }

        Instant now = Instant.now(clock);
        ParkingSpot updated = spot.reassign(newApartmentId, now).rename(newIdentifier, now);
        return parkingSpotRepository.save(updated);
    }
}
