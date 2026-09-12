package com.aurora.parking.camera.application.port;


/**
 * Porta para comunicar mudança administrativa de status a
 * HikvisionAlertStreamManager (componente já existente na Fase 5).
 *
 * STATUS: NÃO CONFIRMADO — não tenho acesso ao código real de
 * HikvisionAlertStreamManager. A implementação real deste adapter
 * deve ser escrita por quem tem o componente em mãos, respeitando
 * a assinatura real da classe. Aqui apenas o contrato de intenção.
 */
public interface CameraStreamControlPort {

    void onCameraEnabled(String cameraCode);

    void onCameraDisabled(String cameraCode);
}