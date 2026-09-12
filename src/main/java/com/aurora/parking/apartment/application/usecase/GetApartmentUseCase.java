package com.aurora.parking.apartment.application.usecase;



import com.aurora.parking.apartment.application.port.ApartmentRepositoryPort;
import com.aurora.parking.apartment.domain.Apartment;
import com.aurora.parking.execption.ApartmentNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GetApartmentUseCase {

    private final ApartmentRepositoryPort repository;

    public GetApartmentUseCase(ApartmentRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Apartment execute(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ApartmentNotFoundException(id));
    }
}