package com.aurora.parking.camera.application.usecase;


import com.aurora.parking.camera.application.port.CameraRepositoryPort;
import com.aurora.parking.camera.domain.Camera;
import com.aurora.parking.execption.CameraNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class UpdateCameraUseCase {

    private final CameraRepositoryPort repository;
    private final Clock clock;

    public UpdateCameraUseCase(CameraRepositoryPort repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Transactional
    public Camera execute(UUID id, String newHost, String newDescription) {
        Camera camera = repository.findById(id)
                .orElseThrow(() -> new CameraNotFoundException(id));

        // "code" e "operation" propositalmente NÃO são editáveis aqui:
        // mudar o code quebraria o casamento com HikvisionCameraConfig;
        // mudar operation em runtime é decisão de negócio sensível (ENTRY/EXIT),
        // exigiria reavaliação de todo Movement em andamento — fora de escopo do CRUD simples.
        Instant now = Instant.now(clock);
        Camera updated = camera.updateDetails(newHost, newDescription, now);
        return repository.save(updated);
    }
}