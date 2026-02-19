package com.example.antipodal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AntipodalCalculatorTest {

    @Test
    void antipodal_returns_opposite_latitude_and_longitude() {
        double lat = 40.7128;   // New York
        double lon = -74.0060;

        double[] result = AntipodalCalculator.antipodal(lat, lon);

        assertEquals(-40.7128, result[0], 1e-6);
        assertEquals(105.994, result[1], 1e-2);  // -74 + 180 = 106
    }
}
