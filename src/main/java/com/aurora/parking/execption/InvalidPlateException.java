package com.aurora.parking.execption;



public class InvalidPlateException extends RuntimeException {
    public InvalidPlateException(String rawPlate) {
        super("Placa inválida: " + rawPlate);
    }
}