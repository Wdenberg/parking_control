package com.aurora.parking.apartment.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ApartmentRequest(

        @NotBlank(message = "Bloco é Obrigatorio")
        @Size(max = 10, message = "Bloco deve ter no minimo 10 caracteres")
        String block,

        @NotBlank(message = "Numero e Obrigatorio")
        @Size(max = 10, message = "numero deve ter no minimo 10 caracteres")
        String number
) { }
