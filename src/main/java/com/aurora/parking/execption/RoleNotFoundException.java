package com.aurora.parking.execption;


public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String code) {
        super("Role não encontrado: " + code);
    }
}