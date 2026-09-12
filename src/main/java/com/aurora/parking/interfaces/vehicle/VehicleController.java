package com.aurora.parking.interfaces.vehicle;

import com.aurora.parking.vehicle.application.vehicle.dto.VehicleRequest;
import com.aurora.parking.vehicle.application.vehicle.dto.VehicleResponse;
import com.aurora.parking.vehicle.application.vehicle.usecase.*;
import com.aurora.parking.vehicle.domain.Vehicle;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private final CreateVehicleUseCase createUseCase;
    private final UpdateVehicleUseCase updateUseCase;
    private final SetVehicleActiveUseCase setActiveUseCase;
    private final GetVehicleUseCase getUseCase;
    private final ListVehiclesUseCase listUseCase;

    public VehicleController(CreateVehicleUseCase createUseCase,
                             UpdateVehicleUseCase updateUseCase,
                             SetVehicleActiveUseCase setActiveUseCase,
                             GetVehicleUseCase getUseCase,
                             ListVehiclesUseCase listUseCase) {
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
        this.setActiveUseCase = setActiveUseCase;
        this.getUseCase = getUseCase;
        this.listUseCase = listUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('VEHICLE_WRITE')")
    public ResponseEntity<VehicleResponse> create(@Valid @RequestBody VehicleRequest request) {
        Vehicle created = createUseCase.execute(
                request.apartmentId(), request.plate(), request.model(), request.color());
        VehicleResponse body = VehicleResponse.from(created, false);
        return ResponseEntity.created(URI.create("/api/v1/vehicles/" + body.id())).body(body);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VEHICLE_READ')")
    public ResponseEntity<VehicleResponse> get(@PathVariable UUID id) {
        Vehicle vehicle = getUseCase.execute(id);
        boolean parked = getUseCase.isCurrentlyParked(id);
        return ResponseEntity.ok(VehicleResponse.from(vehicle, parked));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VEHICLE_READ')")
    public ResponseEntity<Page<VehicleResponse>> list(
            @RequestParam(required = false) UUID apartmentId,
            @RequestParam(required = false) Boolean active,
            Pageable pageable) {
        Page<VehicleResponse> page = listUseCase.execute(pageable, apartmentId, active)
                .map(vehicle -> VehicleResponse.from(vehicle, getUseCase.isCurrentlyParked(vehicle.id())));
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('VEHICLE_WRITE')")
    public ResponseEntity<VehicleResponse> update(@PathVariable UUID id,
                                                  @Valid @RequestBody VehicleRequest request) {
        Vehicle updated = updateUseCase.execute(
                id, request.apartmentId(), request.plate(), request.model(), request.color());
        boolean parked = getUseCase.isCurrentlyParked(id);
        return ResponseEntity.ok(VehicleResponse.from(updated, parked));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('VEHICLE_WRITE')")
    public ResponseEntity<VehicleResponse> deactivate(@PathVariable UUID id) {
        Vehicle updated = setActiveUseCase.execute(id, false);
        return ResponseEntity.ok(VehicleResponse.from(updated, false));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('VEHICLE_WRITE')")
    public ResponseEntity<VehicleResponse> activate(@PathVariable UUID id) {
        Vehicle updated = setActiveUseCase.execute(id, true);
        return ResponseEntity.ok(VehicleResponse.from(updated, false));
    }
}