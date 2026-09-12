package com.aurora.parking.execption;

public class DuplicatePlateException extends RuntimeException {
    public DuplicatePlateException(String plate) {
        super("Já existe veículo cadastrado com a placa: " + plate);
    }
}