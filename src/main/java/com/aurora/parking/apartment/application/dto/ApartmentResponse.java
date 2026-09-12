package com.aurora.parking.apartment.application.dto;

import com.aurora.parking.apartment.domain.Apartment;

import java.time.Instant;
import java.util.UUID;

public record ApartmentResponse(

        UUID id,
        String block,
        String number,
        String identifier,
        boolean active,
        Instant createdAt,
        Instant updatedAT
){

    public static ApartmentResponse from (Apartment apartment){
        return new ApartmentResponse(
                apartment.id(),
                apartment.block(),
                apartment.number(),
                apartment.idetifier(),
                apartment.active(),
                apartment.createdAt(),
                apartment.updatedAt()
        );
    }

}
