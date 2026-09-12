package com.aurora.parking.execption;

public class DuplicateCameraCodeException extends RuntimeException {
    public DuplicateCameraCodeException(String code) {
        super("Já existe câmera cadastrada com o código: " + code);
    }
}
