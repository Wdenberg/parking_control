package com.aurora.parking.infrastructure.persistence.security;


import com.aurora.parking.security.application.PasswordEncoderPort;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Argon2PasswordEncoderAdapter implements PasswordEncoderPort {

    // Parâmetros recomendados pelo OWASP para Argon2id (2024+):
    // saltLength=16, hashLength=32, parallelism=1, memory=19456 (19 MiB), iterations=2
    private final Argon2PasswordEncoder delegate =
            Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    @Override
    public String encode(String rawPassword) {
        return delegate.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedHash) {
        return delegate.matches(rawPassword, encodedHash);
    }
}