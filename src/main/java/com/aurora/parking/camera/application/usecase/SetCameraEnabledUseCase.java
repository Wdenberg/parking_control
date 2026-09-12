package com.aurora.parking.camera.application.usecase;


import com.aurora.parking.camera.application.port.CameraRepositoryPort;
import com.aurora.parking.camera.domain.Camera;
import com.aurora.parking.execption.CameraNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
public class SetCameraEnabledUseCase {

    private final CameraRepositoryPort repository;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    public SetCameraEnabledUseCase(CameraRepositoryPort repository,
                                   ApplicationEventPublisher eventPublisher,
                                   Clock clock) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    @Transactional
    public Camera execute(UUID id, boolean enabled) {
        Camera camera = repository.findById(id)
                .orElseThrow(() -> new CameraNotFoundException(id));

        Instant now = Instant.now(clock);
        Camera updated = enabled ? camera.enable(now) : camera.disable(now);
        Camera saved = repository.save(updated);

        // Publica evento AFTER_COMMIT (mesma disciplina do AGENT.md §27) —
        // quem for religar/desligar o stream real reage aqui, sem acoplar
        // este use case a HikvisionAlertStreamManager diretamente.
        eventPublisher.publishEvent(new CameraEnabledStateChangedEvent(saved.code(), enabled));
        return saved;
    }

    public record CameraEnabledStateChangedEvent(String cameraCode, boolean enabled) {}
}