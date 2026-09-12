package com.aurora.parking.apartment.application.usecase;

import com.aurora.parking.apartment.application.port.ApartmentRepositoryPort;
import com.aurora.parking.apartment.domain.Apartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListApartmentsUseCase {

    private final ApartmentRepositoryPort repository;

    public ListApartmentsUseCase(ApartmentRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Apartment> execute(Pageable pageable, Boolean activeFilter) {
        return repository.findAll(pageable, activeFilter);
    }
}