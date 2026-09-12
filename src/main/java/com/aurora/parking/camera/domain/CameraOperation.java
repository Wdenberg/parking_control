package com.aurora.parking.camera.domain;

/**
 * Já existe conceito de CameraOperation no pipeline LPR (AGENT.md §9/§15).
 * Reaproveitar o mesmo enum se já existir em outro pacote —
 * NÃO duplicar. Declarado aqui como referência caso ainda não exista
 * um local canônico compartilhado.
 */
public enum CameraOperation {
    ENTRY,
    EXIT
}