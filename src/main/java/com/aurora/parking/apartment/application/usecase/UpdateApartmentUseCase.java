package com.aurora.parking.apartment.application.usecase;


import com.aurora.parking.apartment.application.port.ApartmentRepositoryPort;
import com.aurora.parking.apartment.domain.Apartment;
import com.aurora.parking.execption.ApartmentNotFoundException;
import com.aurora.parking.execption.DuplicateApartmentException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class UpdateApartmentUseCase {

    private final ApartmentRepositoryPort repository;
    private final Clock clock;

    public UpdateApartmentUseCase(ApartmentRepositoryPort repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Transactional
    public Apartment execute(UUID id, String newBlock, String newNumber) {
        Apartment apartment = repository.findById(id)
                .orElseThrow(() -> new ApartmentNotFoundException(id));

        if (repository.existsByBlockAndNumberAndIdNot(newBlock, newNumber, id)) {
            throw new DuplicateApartmentException(newBlock, newNumber);
        }

        Instant now = Instant.now(clock);
        Apartment updated = apartment.rename(newBlock, newNumber, now);
        return repository.save(updated);
    }
}