package com.aurora.parking.execption;

/**
 * Mensagem genérica de propósito — nunca revelar se foi o
 * username ou a senha que falhou (evita enumeração de usuários).
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Credenciais inválidas");
    }
}