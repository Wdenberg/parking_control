package com.aurora.parking.execption;

import java.util.UUID;

/**
 * Lançada ao tentar desabilitar ou reatribuir uma vaga que
 * possui um Movement OPEN no momento (INV-02).
 */
public class ParkingSpotOccupiedException extends RuntimeException {
    public ParkingSpotOccupiedException(UUID parkingSpotId) {
        super("Vaga ocupada no momento, operação não permitida: " + parkingSpotId);
    }
}
