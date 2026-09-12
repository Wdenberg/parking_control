package com.aurora.parking.vehicle.domain;

import com.aurora.parking.execption.InvalidPlateException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlateNormalizerTest {

    @Test
    void normalizesAllowedSeparatorsAndCase() {
        assertEquals("ABC1234", PlateNormalizer.normalize(" abc-1234 "));
    }

    @Test
    void rejectsInvalidPlate() {
        assertThrows(InvalidPlateException.class, () -> PlateNormalizer.normalize("ABC"));
    }
}
