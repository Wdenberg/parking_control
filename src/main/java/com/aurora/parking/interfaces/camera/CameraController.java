package com.aurora.parking.interfaces.camera;

import com.aurora.parking.camera.application.dto.CameraRequest;
import com.aurora.parking.camera.application.dto.CameraResponse;
import com.aurora.parking.camera.application.usecase.*;
import com.aurora.parking.camera.domain.Camera;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cameras")
public class CameraController {

    private final CreateCameraUseCase createUseCase;
    private final UpdateCameraUseCase updateUseCase;
    private final SetCameraEnabledUseCase setEnabledUseCase;
    private final GetCameraUseCase getUseCase;
    private final ListCamerasUseCase listUseCase;

    public CameraController(CreateCameraUseCase createUseCase,
                            UpdateCameraUseCase updateUseCase,
                            SetCameraEnabledUseCase setEnabledUseCase,
                            GetCameraUseCase getUseCase,
                            ListCamerasUseCase listUseCase) {
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
        this.setEnabledUseCase = setEnabledUseCase;
        this.getUseCase = getUseCase;
        this.listUseCase = listUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CAMERA_WRITE')")
    public ResponseEntity<CameraResponse> create(@Valid @RequestBody CameraRequest request) {
        Camera created = createUseCase.execute(
                request.code(), request.host(), request.operation(), request.description());
        CameraResponse body = CameraResponse.from(created);
        return ResponseEntity.created(URI.create("/api/v1/cameras/" + body.id())).body(body);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CAMERA_READ')")
    public ResponseEntity<CameraResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(CameraResponse.from(getUseCase.execute(id)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CAMERA_READ')")
    public ResponseEntity<Page<CameraResponse>> list(
            @RequestParam(required = false) Boolean enabled,
            Pageable pageable) {
        Page<CameraResponse> page = listUseCase.execute(pageable, enabled).map(CameraResponse::from);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CAMERA_WRITE')")
    public ResponseEntity<CameraResponse> update(@PathVariable UUID id,
                                                 @Valid @RequestBody CameraRequest request) {
        Camera updated = updateUseCase.execute(id, request.host(), request.description());
        return ResponseEntity.ok(CameraResponse.from(updated));
    }

    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasAuthority('CAMERA_WRITE')")
    public ResponseEntity<CameraResponse> disable(@PathVariable UUID id) {
        return ResponseEntity.ok(CameraResponse.from(setEnabledUseCase.execute(id, false)));
    }

    @PatchMapping("/{id}/enable")
    @PreAuthorize("hasAuthority('CAMERA_WRITE')")
    public ResponseEntity<CameraResponse> enable(@PathVariable UUID id) {
        return ResponseEntity.ok(CameraResponse.from(setEnabledUseCase.execute(id, true)));
    }
}