package com.aurora.parking.execption;

public class DuplicateParkingSpotException extends RuntimeException {
    public DuplicateParkingSpotException(String identifier) {
        super("Vaga já existe com identifier: " + identifier);
    }
}