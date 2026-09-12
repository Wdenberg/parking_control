package com.aurora.parking.execption;

public class DuplicateApartmentException extends RuntimeException{

    public DuplicateApartmentException(String block, String number){
        super("Apartamento já exite: " + block + "numero: " + number);
    }

}
