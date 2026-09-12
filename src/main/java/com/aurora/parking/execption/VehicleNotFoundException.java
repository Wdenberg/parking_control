package com.aurora.parking.execption;

import java.util.UUID;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(UUID id) {
        super("Veículo não encontrado: " + id);
    }
}