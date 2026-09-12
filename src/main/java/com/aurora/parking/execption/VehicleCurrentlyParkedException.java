package com.aurora.parking.execption;

import java.util.UUID;

/**
 * Lançada ao tentar desativar/reatribuir um veículo que possui
 * um Movement OPEN (está fisicamente estacionado agora).
 */
public class VehicleCurrentlyParkedException extends RuntimeException {
    public VehicleCurrentlyParkedException(UUID vehicleId) {
        super("Veículo está estacionado no momento, operação não permitida: " + vehicleId);
    }
}