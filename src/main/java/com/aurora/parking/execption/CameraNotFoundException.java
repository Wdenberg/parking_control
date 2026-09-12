package com.aurora.parking.execption;

import java.util.UUID;

public class CameraNotFoundException extends RuntimeException {
    public CameraNotFoundException(UUID id) {
        super("Câmera não encontrada: " + id);
    }
}