package com.aurora.parking.parkingspot.application.parkingspot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ParkingSpotRequest(
        @NotNull(message = "apartmentId é obrigatório")
        UUID apartmentId,

        @NotBlank(message = "identifier é obrigatório")
        @Size(max = 10, message = "identifier deve ter no máximo 10 caracteres")
        String identifier
) {}