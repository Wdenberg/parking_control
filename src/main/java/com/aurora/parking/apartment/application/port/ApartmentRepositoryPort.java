package com.aurora.parking.apartment.application.port;

import com.aurora.parking.apartment.domain.Apartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ApartmentRepositoryPort {

    Apartment save(Apartment apartment);
    Optional<Apartment> findById(UUID id);
    boolean existsByBlockAndNumber(String block, String number);
    boolean existsByBlockAndNumberAndIdNot(String block, String number, UUID excludingId);
    Page<Apartment> findAll(Pageable pageable, Boolean activeFilter);
}
