package com.aurora.parking.vehicle.application.vehicle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record VehicleRequest(
        @NotNull(message = "apartmentId é obrigatório")
        UUID apartmentId,

        @NotBlank(message = "plate é obrigatória")
        @Size(max = 10, message = "plate deve ter no máximo 10 caracteres brutos")
        String plate,

        @Size(max = 60)
        String model,

        @Size(max = 30)
        String color
) {}