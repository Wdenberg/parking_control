package com.aurora.parking.execption;

import java.util.UUID;

public class ApartmentNotFoundException  extends RuntimeException{

    public ApartmentNotFoundException(UUID id){
        super("Apartamento não Encontrado: " + id);
    }
}
