package com.aurora.parking.execption;

import java.util.UUID;

public class ParkingSpotNotFoundException extends RuntimeException {
    public ParkingSpotNotFoundException(UUID id) {
        super("Vaga não encontrada: " + id);
    }
}