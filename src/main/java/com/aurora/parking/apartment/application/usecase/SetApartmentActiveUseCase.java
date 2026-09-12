package com.aurora.parking.apartment.application.usecase;


import com.aurora.parking.apartment.application.port.ApartmentRepositoryPort;
import com.aurora.parking.apartment.domain.Apartment;
import com.aurora.parking.execption.ApartmentNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class SetApartmentActiveUseCase {

    private final ApartmentRepositoryPort repository;
    private final Clock clock;

    public SetApartmentActiveUseCase(ApartmentRepositoryPort repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Transactional
    public Apartment execute(UUID id, boolean active) {
        Apartment apartment = repository.findById(id)
                .orElseThrow(() -> new ApartmentNotFoundException(id));

        Instant now = Instant.now(clock);
        Apartment updated = active ? apartment.activate(now) : apartment.deactivate(now);
        return repository.save(updated);
    }
}