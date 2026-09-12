package com.aurora.parking.camera.application.usecase;

import com.aurora.parking.camera.application.port.CameraStreamControlPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Reage à mudança administrativa de status DEPOIS do commit (AGENT.md §27).
 * A implementação real de CameraStreamControlPort ainda não existe —
 * ver nota "STATUS: NÃO CONFIRMADO" no port.
 */
@Component
public class CameraStreamStateListener {

    private final CameraStreamControlPort streamControlPort;

    public CameraStreamStateListener(CameraStreamControlPort streamControlPort) {
        this.streamControlPort = streamControlPort;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCameraStateChanged(SetCameraEnabledUseCase.CameraEnabledStateChangedEvent event) {
        if (event.enabled()) {
            streamControlPort.onCameraEnabled(event.cameraCode());
        } else {
            streamControlPort.onCameraDisabled(event.cameraCode());
        }
    }
}