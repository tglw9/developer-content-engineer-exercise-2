package com.example.antipodal;

/**
 * Calculates the antipodal point (diametrically opposite point on Earth)
 * for a given latitude and longitude.
 */
public final class AntipodalCalculator {

    private AntipodalCalculator() {}

    /**
     * Computes the antipodal latitude (negate the given latitude).
     */
    public static double antipodalLatitude(double latitude) {
        return -latitude;
    }

    /**
     * Computes the antipodal longitude (add 180° and normalize to -180..180).
     */
    public static double antipodalLongitude(double longitude) {
        double result = longitude + 180.0;
        if (result > 180.0) {
            result -= 360.0;
        } else if (result <= -180.0) {
            result += 360.0;
        }
        return result;
    }

    /**
     * Returns the antipodal point as a two-element array [latitude, longitude].
     */
    public static double[] antipodal(double latitude, double longitude) {
        return new double[] {
            antipodalLatitude(latitude),
            antipodalLongitude(longitude)
        };
    }
}
