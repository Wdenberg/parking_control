package com.aurora.parking.interfaces.apartment;


import com.aurora.parking.apartment.application.dto.ApartmentRequest;
import com.aurora.parking.apartment.application.dto.ApartmentResponse;
import com.aurora.parking.apartment.application.usecase.*;
import com.aurora.parking.apartment.domain.Apartment;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

/**
 * Controller fino: apenas tradução HTTP <-> use case.
 * Nenhuma regra de negócio aqui (regra proibida do AGENT.md §46).
 */
@RestController
@RequestMapping("/api/v1/apartments")
public class ApartmentController {

    private final CreateApartmentUseCase createApartmentUseCase;
    private final UpdateApartmentUseCase updateApartmentUseCase;
    private final SetApartmentActiveUseCase setApartmentActiveUseCase;
    private final GetApartmentUseCase getApartmentUseCase;
    private final ListApartmentsUseCase listApartmentsUseCase;

    public ApartmentController(CreateApartmentUseCase createApartmentUseCase,
                               UpdateApartmentUseCase updateApartmentUseCase,
                               SetApartmentActiveUseCase setApartmentActiveUseCase,
                               GetApartmentUseCase getApartmentUseCase,
                               ListApartmentsUseCase listApartmentsUseCase) {
        this.createApartmentUseCase = createApartmentUseCase;
        this.updateApartmentUseCase = updateApartmentUseCase;
        this.setApartmentActiveUseCase = setApartmentActiveUseCase;
        this.getApartmentUseCase = getApartmentUseCase;
        this.listApartmentsUseCase = listApartmentsUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('APARTMENT_WRITE')")
    public ResponseEntity<ApartmentResponse> create(@Valid @RequestBody ApartmentRequest request) {
        Apartment created = createApartmentUseCase.execute(request.block(), request.number());
        ApartmentResponse body = ApartmentResponse.from(created);
        return ResponseEntity.created(URI.create("/api/v1/apartments/" + body.id())).body(body);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('APARTMENT_READ')")
    public ResponseEntity<ApartmentResponse> get(@PathVariable UUID id) {
        Apartment apartment = getApartmentUseCase.execute(id);
        return ResponseEntity.ok(ApartmentResponse.from(apartment));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('APARTMENT_READ')")
    public ResponseEntity<Page<ApartmentResponse>> list(
            @RequestParam(required = false) Boolean active,
            Pageable pageable) {
        Page<ApartmentResponse> page = listApartmentsUseCase.execute(pageable, active)
                .map(ApartmentResponse::from);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('APARTMENT_WRITE')")
    public ResponseEntity<ApartmentResponse> update(@PathVariable UUID id,
                                                    @Valid @RequestBody ApartmentRequest request) {
        Apartment updated = updateApartmentUseCase.execute(id, request.block(), request.number());
        return ResponseEntity.ok(ApartmentResponse.from(updated));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('APARTMENT_WRITE')")
    public ResponseEntity<ApartmentResponse> deactivate(@PathVariable UUID id) {
        Apartment updated = setApartmentActiveUseCase.execute(id, false);
        return ResponseEntity.ok(ApartmentResponse.from(updated));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('APARTMENT_WRITE')")
    public ResponseEntity<ApartmentResponse> activate(@PathVariable UUID id) {
        Apartment updated = setApartmentActiveUseCase.execute(id, true);
        return ResponseEntity.ok(ApartmentResponse.from(updated));
    }
}