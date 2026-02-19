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

    /**
     * Validates if the given latitude is within valid range (-90 to 90 degrees).
     * @param latitude the latitude to validate
     * @return true if latitude is valid, false otherwise
     */
    public static boolean isValidLatitude(double latitude) {
        return latitude >= -90.0 && latitude <= 90.0;
    }

    /**
     * Validates if the given longitude is within valid range (-180 to 180 degrees).
     * @param longitude the longitude to validate
     * @return true if longitude is valid, false otherwise
     */
    public static boolean isValidLongitude(double longitude) {
        return longitude >= -180.0 && longitude <= 180.0;
    }

    /**
     * Validates if both latitude and longitude are within valid ranges.
     * @param latitude the latitude to validate
     * @param longitude the longitude to validate
     * @return true if both coordinates are valid, false otherwise
     */
    public static boolean isValidCoordinates(double latitude, double longitude) {
        return isValidLatitude(latitude) && isValidLongitude(longitude);
    }

    /**
     * Calculates the antipodal point with validation.
     * Throws IllegalArgumentException if coordinates are invalid.
     * @param latitude the latitude (-90 to 90)
     * @param longitude the longitude (-180 to 180)
     * @return the antipodal point as [latitude, longitude]
     * @throws IllegalArgumentException if coordinates are out of valid range
     */
    public static double[] antipodalWithValidation(double latitude, double longitude) {
        if (!isValidCoordinates(latitude, longitude)) {
            throw new IllegalArgumentException(
                String.format("Invalid coordinates: latitude=%.4f, longitude=%.4f", latitude, longitude)
            );
        }
        return antipodal(latitude, longitude);
    }

    /**
     * Checks if a given point is in the Northern Hemisphere (latitude > 0).
     * @param latitude the latitude to check
     * @return true if in Northern Hemisphere, false otherwise
     */
    public static boolean isNorthernHemisphere(double latitude) {
        return latitude > 0.0;
    }

    /**
     * Checks if a given point is in the Eastern Hemisphere (longitude > 0).
     * @param longitude the longitude to check
     * @return true if in Eastern Hemisphere, false otherwise
     */
    public static boolean isEasternHemisphere(double longitude) {
        return longitude > 0.0;
    }
}
