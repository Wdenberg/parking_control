package com.aurora.parking.execption;

public class UserDisabledException extends RuntimeException {
    public UserDisabledException(String username) {
        super("Usuário desabilitado: " + username);
    }
}