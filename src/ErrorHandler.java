import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import swiftbot.SwiftBotAPI;

import swiftbot.SwiftBotAPI;

public class ErrorHandler {

    /**
     * Handles invalid user input (e.g., pressing the wrong button or entering an invalid character).
     * Provides clear feedback to the user and prompts them to try again.
     *
     * @param message The error message to display.
     */
    public void handleInvalidInput(String message) {
        System.out.println("Error: " + message);
        System.out.println("Please press the correct button (A or B) and try again.");
    }

    /**
     * Handles QR code errors (e.g., invalid QR code content or no QR code detected).
     * Provides clear feedback to the user and prompts them to scan a valid QR code.
     *
     * @param message The error message to display.
     */
    public void handleQRCodeError(String message) {
        System.out.println("QR Code Error: " + message);
        System.out.println("Please ensure the QR code contains valid hexadecimal values (1-2 digits, separated by colons).");
    }

    /**
     * Checks the SwiftBot connection status by attempting to use the camera.
     * If the camera fails to initialize or throws an exception, the SwiftBot is considered disconnected.
     *
     * @param swiftbot The SwiftBotAPI instance to check.
     */
    public void checkSwiftBotConnection(SwiftBotAPI swiftbot) {
        try {
            // Attempt to use the camera to check the connection
            BufferedImage testImage = swiftbot.getQRImage();
            if (testImage == null) {
                handleError("SwiftBot camera is not functioning. Check power and connections.", ErrorType.CRITICAL);
            }
        } catch (Exception e) {
            handleError("SwiftBot is disconnected or not responding: " + e.getMessage(), ErrorType.CRITICAL);
        }
    }

    /**
     * Handles generic errors with a specified severity level.
     * Provides clear feedback to the user and logs the error for debugging.
     *
     * @param message The error message to display.
     * @param type    The severity level of the error (e.g., CRITICAL, WARNING).
     */
    public void handleError(String message, ErrorType type) {
        switch (type) {
            case CRITICAL:
                System.out.println("CRITICAL ERROR: " + message);
                System.exit(1); // Terminate the program for critical errors
                break;
            case WARNING:
                System.out.println("WARNING: " + message);
                break;
            default:
                System.out.println("ERROR: " + message);
                break;
        }
    }

    /**
     * Enum to define the severity level of errors.
     */
    public enum ErrorType {
        CRITICAL, WARNING
    }

    /**
     * Displays the contents of the log file.
     *
     * @param logFilePath The path to the log file.
     */
    public void displayLogFile(String logFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            System.out.println("\nLog File Contents:");
            System.out.println("==================");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("==================");
        } catch (IOException e) {
            System.out.println("Error reading log file: " + e.getMessage());
        }
    }
}