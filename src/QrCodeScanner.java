import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Scanner;

import swiftbot.*;

public class QrCodeScanner {
    private SwiftBotAPI swiftbot;

    public QrCodeScanner(SwiftBotAPI swiftbot) {
        this.swiftbot = swiftbot;
    }

    /**
     * Scans a QR code and extracts valid hexadecimal values separated by colons.
     * - Ensures only up to 5 valid hex values are processed.
     * - Provides detailed error messages for invalid inputs.
     *
     * @return A list of valid hexadecimal values (up to 5).
     */
    public ArrayList<String> scanQRCode() {
        ArrayList<String> validValues = new ArrayList<>();
        boolean qrScannedSuccessfully = false;

        while (!qrScannedSuccessfully) {
            System.out.println("Position the QR code and press ENTER to scan...");
            new Scanner(System.in).nextLine(); // Wait for user input

            BufferedImage QR = swiftbot.getQRImage(); // Capture the QR code image
            String qrContent = swiftbot.decodeQRImage(QR); // Decode the QR code content

            if (qrContent == null || qrContent.isEmpty()) {
                System.out.println("Error: No QR code detected. Please try again.");
                continue;
            }

            // Split the QR content by colons (:) as specified in the brief
            String[] parts = qrContent.split(":");

            // Process each part and validate it
            for (String part : parts) {
                if (isValidHex(part)) {
                    validValues.add(part.toUpperCase()); // Add valid values to the list
                } else {
                    System.out.println("Invalid value skipped: " + part + " (must be 1-2 digit hex)");
                }
            }

            if (!validValues.isEmpty()) {
                // Enforce the 5-value limit as specified in the brief
                if (validValues.size() > 5) {
                    System.out.println("Warning: Only the first 5 valid hexadecimal values will be processed.");
                    validValues = new ArrayList<>(validValues.subList(0, 5)); // Keep only the first 5 values
                }

                qrScannedSuccessfully = true; // Exit the loop after successful processing
            } else {
                System.out.println("No valid hexadecimal values found in the QR code. Please try again.");
            }
        }

        return validValues;
    }

    /**
     * Validates if a string is a valid 1- or 2-digit hexadecimal number.
     * - Uses regex to ensure the input matches the pattern for hex values.
     *
     * @param hex The string to validate.
     * @return True if the string is a valid hexadecimal number, false otherwise.
     */
    private boolean isValidHex(String hex) {
        return hex.matches("^[0-9A-Fa-f]{1,2}$"); // Regex for 1-2 digit hex values
    }
}