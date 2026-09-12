package com.aurora.parking.infrastructure.persistence.camera;

import com.aurora.parking.camera.application.port.CameraStreamControlPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Implementação temporária — apenas loga a intenção.
 * NÃO conecta/desconecta o stream real. Substituir por um adapter
 * que chame HikvisionAlertStreamManager quando o componente real
 * estiver disponível para integração (regra §44 — não inventar
 * comportamento de componente que não foi inspecionado).
 */
@Component
public class NoOpCameraStreamControlAdapter implements CameraStreamControlPort {

    private static final Logger log = LoggerFactory.getLogger(NoOpCameraStreamControlAdapter.class);

    @Override
    public void onCameraEnabled(String cameraCode) {
        log.warn("Câmera {} habilitada administrativamente, mas o stream real NÃO foi religado automaticamente (adapter não implementado)", cameraCode);
    }

    @Override
    public void onCameraDisabled(String cameraCode) {
        log.warn("Câmera {} desabilitada administrativamente, mas o stream real NÃO foi desligado automaticamente (adapter não implementado)", cameraCode);
    }
}