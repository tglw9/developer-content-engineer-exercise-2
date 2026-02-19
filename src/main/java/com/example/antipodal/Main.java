package com.example.antipodal;

import java.util.Scanner;

/**
 * Simple CLI that reads latitude and longitude from the user
 * and prints the antipodal point.
 */
public final class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter latitude: ");
            double lat = scanner.nextDouble();
            System.out.print("Enter longitude: ");
            double lon = scanner.nextDouble();

            double[] antipodal = AntipodalCalculator.antipodal(lat, lon);
            System.out.printf("Antipodal point: latitude %.4f, longitude %.4f%n",
                antipodal[0], antipodal[1]);
        }
    }
}
