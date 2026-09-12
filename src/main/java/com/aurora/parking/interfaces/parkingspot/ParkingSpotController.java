package com.aurora.parking.interfaces.parkingspot;


import com.aurora.parking.parkingspot.application.parkingspot.dto.ParkingSpotRequest;
import com.aurora.parking.parkingspot.application.parkingspot.dto.ParkingSpotResponse;
import com.aurora.parking.parkingspot.application.parkingspot.usecase.*;
import com.aurora.parking.parkingspot.domain.ParkingSpot;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/parking-spots")
public class ParkingSpotController {

    private final CreateParkingSpotUseCase createUseCase;
    private final UpdateParkingSpotUseCase updateUseCase;
    private final SetParkingSpotEnabledUseCase setEnabledUseCase;
    private final GetParkingSpotUseCase getUseCase;
    private final ListParkingSpotsUseCase listUseCase;

    public ParkingSpotController(CreateParkingSpotUseCase createUseCase,
                                 UpdateParkingSpotUseCase updateUseCase,
                                 SetParkingSpotEnabledUseCase setEnabledUseCase,
                                 GetParkingSpotUseCase getUseCase,
                                 ListParkingSpotsUseCase listUseCase) {
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
        this.setEnabledUseCase = setEnabledUseCase;
        this.getUseCase = getUseCase;
        this.listUseCase = listUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PARKING_WRITE')")
    public ResponseEntity<ParkingSpotResponse> create(@Valid @RequestBody ParkingSpotRequest request) {
        ParkingSpot created = createUseCase.execute(request.apartmentId(), request.identifier());
        ParkingSpotResponse body = ParkingSpotResponse.from(created, false);
        return ResponseEntity.created(URI.create("/api/v1/parking-spots/" + body.id())).body(body);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PARKING_READ')")
    public ResponseEntity<ParkingSpotResponse> get(@PathVariable UUID id) {
        ParkingSpot spot = getUseCase.execute(id);
        boolean occupied = getUseCase.isCurrentlyOccupied(id);
        return ResponseEntity.ok(ParkingSpotResponse.from(spot, occupied));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PARKING_READ')")
    public ResponseEntity<Page<ParkingSpotResponse>> list(
            @RequestParam(required = false) UUID apartmentId,
            @RequestParam(required = false) Boolean enabled,
            Pageable pageable) {
        Page<ParkingSpotResponse> page = listUseCase.execute(pageable, apartmentId, enabled)
                .map(spot -> ParkingSpotResponse.from(spot, getUseCase.isCurrentlyOccupied(spot.id())));
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PARKING_WRITE')")
    public ResponseEntity<ParkingSpotResponse> update(@PathVariable UUID id,
                                                      @Valid @RequestBody ParkingSpotRequest request) {
        ParkingSpot updated = updateUseCase.execute(id, request.apartmentId(), request.identifier());
        boolean occupied = getUseCase.isCurrentlyOccupied(id);
        return ResponseEntity.ok(ParkingSpotResponse.from(updated, occupied));
    }

    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasAuthority('PARKING_WRITE')")
    public ResponseEntity<ParkingSpotResponse> disable(@PathVariable UUID id) {
        ParkingSpot updated = setEnabledUseCase.execute(id, false);
        return ResponseEntity.ok(ParkingSpotResponse.from(updated, false));
    }

    @PatchMapping("/{id}/enable")
    @PreAuthorize("hasAuthority('PARKING_WRITE')")
    public ResponseEntity<ParkingSpotResponse> enable(@PathVariable UUID id) {
        ParkingSpot updated = setEnabledUseCase.execute(id, true);
        return ResponseEntity.ok(ParkingSpotResponse.from(updated, false));
    }
}