package com.aurora.parking.execption;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String username) {
        super("Já existe usuário com o username: " + username);
    }
}