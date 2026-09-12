package com.aurora.parking.camera.application.dto;


import com.aurora.parking.camera.domain.CameraOperation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CameraRequest(
        @NotBlank(message = "code é obrigatório")
        @Size(max = 20)
        String code,

        @Size(max = 100)
        String host,

        @NotNull(message = "operation é obrigatório")
        CameraOperation operation,

        @Size(max = 200)
        String description
) {}