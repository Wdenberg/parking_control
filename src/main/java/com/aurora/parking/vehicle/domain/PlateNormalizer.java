package com.aurora.parking.vehicle.domain;

import com.aurora.parking.execption.InvalidPlateException;

import java.util.regex.Pattern;

/**
 * Normalização de placa — regra de negócio pura (AGENT.md §20).
 * Usada tanto pelo CRUD administrativo quanto pelo pipeline LPR
 * (via LprEventNormalizer, em application).
 */
public final class PlateNormalizer {

    private static final Pattern ALLOWED_SEPARATORS = Pattern.compile("[\\s\\-.]");
    private static final Pattern VALID_PLATE = Pattern.compile("^[A-Z0-9]{6,8}$");

    private PlateNormalizer() {
    }

    public static String normalize(String rawPlate) {
        if (rawPlate == null) {
            throw new InvalidPlateException("null");
        }
        String cleaned = ALLOWED_SEPARATORS.matcher(rawPlate.trim().toUpperCase()).replaceAll("");
        if (!VALID_PLATE.matcher(cleaned).matches()) {
            throw new InvalidPlateException(rawPlate);
        }
        return cleaned;
    }
}