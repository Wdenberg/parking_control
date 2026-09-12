package com.aurora.parking.parkingspot.application.parkingspot.usecase;


import com.aurora.parking.parkingspot.application.parkingspot.port.ParkingSpotRepositoryPort;
import com.aurora.parking.parkingspot.domain.ParkingSpot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ListParkingSpotsUseCase {

    private final ParkingSpotRepositoryPort repository;

    public ListParkingSpotsUseCase(ParkingSpotRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<ParkingSpot> execute(Pageable pageable, UUID apartmentIdFilter, Boolean enabledFilter) {
        return repository.findAll(pageable, apartmentIdFilter, enabledFilter);
    }
}