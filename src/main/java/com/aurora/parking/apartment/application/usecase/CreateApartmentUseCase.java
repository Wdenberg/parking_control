package com.aurora.parking.apartment.application.usecase;

import com.aurora.parking.apartment.application.port.ApartmentRepositoryPort;
import com.aurora.parking.apartment.domain.Apartment;
import com.aurora.parking.execption.DuplicateApartmentException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class CreateApartmentUseCase {

    private final ApartmentRepositoryPort repository;
    private final Clock clock;


    public CreateApartmentUseCase(ApartmentRepositoryPort repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Transactional
    public Apartment execute(String block, String number){
        if(repository.existsByBlockAndNumber(block, number)){
            throw new DuplicateApartmentException(block, number);
        }
        Instant now = Instant.now(clock);
        Apartment apartment = Apartment.create(UUID.randomUUID(), block, number, now);
        return repository.save(apartment);
    }
}
