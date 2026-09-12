package com.aurora.parking.apartment.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record Apartment(
        UUID id,
        String block,
        String number,
        String identifier,
        boolean active,
        Instant createdAt,
        Instant updatedAt

){

    public Apartment{
        Objects.requireNonNull(block, "block não pode ser nulo");
        Objects.requireNonNull(number, "number não pode ser nulo");
        if(block.isBlank() || number.isBlank()){
            throw new IllegalArgumentException("block e number não pode ser vazios");
        }
    }
    public static Apartment create(UUID id, String block, String number, Instant now){
        String identifier = block.trim().toUpperCase() + "-" + number.trim();
        return new Apartment(id, block.trim(),number.trim(), identifier, true, now, now);
    }

    public Apartment rename(String newBlock, String newNumber,Instant now){
        String identifier = newBlock.trim().toUpperCase() + "-" + number.trim();
        return new Apartment(id, newBlock.trim(), newNumber.trim(), identifier, active, createdAt, now);
    }
    public Apartment deactivate(Instant now){
        if(!active){
            return  this;
        }
        return new Apartment(id, block, number, identifier, false, createdAt, now);
    }
    public Apartment activate(Instant now){
        if(active){
            return  this;
        }
        return new Apartment(id, block, number, identifier, true, createdAt, now);
    }
}
