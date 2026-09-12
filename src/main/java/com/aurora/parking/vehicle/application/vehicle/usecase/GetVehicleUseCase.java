package com.aurora.parking.vehicle.application.vehicle.usecase;

import com.aurora.parking.execption.VehicleNotFoundException;
import com.aurora.parking.vehicle.application.vehicle.port.VehicleRepositoryPort;
import com.aurora.parking.vehicle.domain.Vehicle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class GetVehicleUseCase {

    private final VehicleRepositoryPort repository;

    public GetVehicleUseCase(VehicleRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Vehicle execute(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
    }

    /**
     * Usado pelo pipeline LPR (ProcessLprEventUseCase) para buscar
     * veículo pela placa normalizada — não lança exceção, retorna Optional
     * porque "veículo desconhecido" é um fluxo de negócio válido, não erro.
     */
    @Transactional(readOnly = true)
    public Optional<Vehicle> findByNormalizedPlate(String normalizedPlate) {
        return repository.findByPlate(normalizedPlate);
    }

    @Transactional(readOnly = true)
    public boolean isCurrentlyParked(UUID id) {
        return repository.isCurrentlyParked(id);
    }
}