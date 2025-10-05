import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import swiftbot.*;

public class SwiftbotDance {
    private static SwiftBotAPI swiftbot;
    private static FileHandler fileHandler;
    private static ErrorHandler errorHandler;
    private static boolean buttonAPressed = false;
    private static boolean buttonBPressed = false;
    private static DemoDance demoDance;

    public static void main(String[] args) throws InterruptedException {
        swiftbot = new SwiftBotAPI();
        fileHandler = new FileHandler();
        errorHandler = new ErrorHandler();
        SpeedCalculator speedCalc = new SpeedCalculator();
        LEDController ledControl = new LEDController(swiftbot);
        demoDance = new DemoDance(swiftbot, speedCalc, ledControl);

        // Check SwiftBot connection
        errorHandler.checkSwiftBotConnection(swiftbot);

        // Set up button listeners for A (continue) and B (exit)
        swiftbot.enableButton(Button.A, () -> buttonAPressed = true);
        swiftbot.enableButton(Button.B, () -> buttonBPressed = true);

        while (true) {
            displayWelcomeInterface();
        }
    }

    private static void displayWelcomeInterface() throws InterruptedException {
        System.out.println("\n================================================");
        System.out.println("      SWIFTBOT DANCE PROGRAM - USING A QR CODE");
        System.out.println("================================================");
        System.out.println("Press 'A' to scan a QR code.");
        System.out.println("Press 'D' to see a demo dance.");
        System.out.println("Press 'L' to view the log file.");
        System.out.println("Press 'B' to exit.");
        System.out.println("================================================");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (buttonAPressed) {
                buttonAPressed = false; // Reset the flag
                scanNewQRCode();
                break;
            } else if (buttonBPressed) {
                buttonBPressed = false; // Reset the flag
                exitProgram();
                break;
            } else {
                try {
                    if (System.in.available() > 0) { // Check if keyboard input is available
                        String input = scanner.nextLine().trim().toLowerCase();
                        if (input.equals("d")) {
                            demoDance.executeDemoDance();
                            break;
                        } else if (input.equals("l")) {
                            viewLogFile();
                            break;
                        } else {
                            errorHandler.handleInvalidInput("Invalid input. Please press 'A', 'B', 'D', or 'L'.");
                        }
                    }
                } catch (IOException e) {
                    // Log the error but do not terminate
                    System.err.println("Error checking for keyboard input: " + e.getMessage());
                }
            }

            Thread.sleep(100); // Prevent CPU overload while waiting for input
        }
    }

    /**
     * Displays the contents of the log file and waits for the user to press 'B' to return to the main UI.
     */
    private static void viewLogFile() throws InterruptedException {
        fileHandler.displayLogFile(); // Display the log file contents

        System.out.println("\nPress 'B' to return to the main menu.");

        // Wait for the B button to be pressed
        while (true) {
            if (buttonBPressed) {
                buttonBPressed = false; // Reset the flag
                break; // Exit the loop and return to the main UI
            }
            Thread.sleep(100); // Prevent CPU overload while waiting for input
        }
    }

    private static void scanNewQRCode() throws InterruptedException {
        System.out.println("\n================================================");
        System.out.println("      SWIFTBOT DANCE PROGRAM - USING A QR CODE");
        System.out.println("================================================");
        System.out.println("Position the QR code in front of the SwiftBot camera...");
        System.out.println("Press the 'A' button to scan.");
        
        // Wait until the A button is pressed
        waitForButtonPressA();

        BufferedImage qrImage = swiftbot.getQRImage();
        String qrContent = swiftbot.decodeQRImage(qrImage);

        if (qrContent == null || qrContent.isEmpty()) {
            errorHandler.handleQRCodeError("No QR code detected or invalid content.");
            return;
        }

        fileHandler.saveQRCodeToLog(qrContent);
        executeDanceRoutine(qrContent);
    }

    private static void executeDanceRoutine(String qrContent) throws InterruptedException {
        ArrayList<String> validValues = new ArrayList<>();
        String[] parts = qrContent.split(":");

        for (String part : parts) {
            if (part.matches("^[0-9A-Fa-f]{1,2}$")) {
                validValues.add(part.toUpperCase());
            } else {
                errorHandler.handleQRCodeError("Invalid hexadecimal value: " + part);
            }
        }

        if (validValues.isEmpty()) {
            errorHandler.handleQRCodeError("No valid hex values found in the QR code.");
            return;
        }

        // Display pre-dance information
        NumberConverter converter = new NumberConverter();
        SpeedCalculator speedCalc = new SpeedCalculator();
        LEDController ledControl = new LEDController(swiftbot);

        for (String hex : validValues) {
            int decimal = converter.hexToDecimal(hex);
            int octal = converter.hexToOctal(hex);
            String binary = converter.hexToBinary(hex);
            int speed = speedCalc.calculateSpeed(octal);

            System.out.println("\nHex: " + hex);
            System.out.println("Octal: " + octal);
            System.out.println("Decimal: " + decimal);
            System.out.println("Binary: " + binary);
            System.out.println("Speed: " + speed);
            System.out.println("LED Color (R, G, B): " + decimal + ", " + (decimal % 80) * 3 + ", " + Math.max(decimal, (decimal % 80) * 3));
        }

        System.out.println("\nStarting dance in 2 seconds...");
        Thread.sleep(2000); // 2-second pause

        // Execute dance routine
        DanceMovement dancer = new DanceMovement(swiftbot, speedCalc);
        for (String hex : validValues) {
            int decimal = converter.hexToDecimal(hex);
            int octal = converter.hexToOctal(hex);
            ledControl.setLEDColor(decimal);
            String binary = converter.hexToBinary(hex);
            dancer.executeDance(binary, hex.length() == 1 ? 1000 : 500, octal);
            ledControl.turnOffLED();
        }

        System.out.println("\nDance complete! Turning off LED...");
        ledControl.turnOffLED();

        // Ask the user if they want to scan another QR code or exit
        promptUserAfterDance();
    }

    private static void promptUserAfterDance() throws InterruptedException {
        System.out.println("\n================================================");
        System.out.println("Dance routine complete.");
        System.out.println("Press 'A' to scan another QR code.");
        System.out.println("Press 'B' to exit and save all hexadecimal values.");
        System.out.println("================================================");

        // Reset button flags to avoid residual presses
        buttonAPressed = false;
        buttonBPressed = false;

        while (true) {
            // Wait for A or B to be pressed
            while (!buttonAPressed && !buttonBPressed) {
                Thread.sleep(100); // Wait for a button press
            }

            // Handle the button press
            if (buttonAPressed) {
                buttonAPressed = false; // Reset the flag
                return; // Restart loop in main() to scan a new QR code
            } else if (buttonBPressed) {
                buttonBPressed = false; // Reset the flag
                exitProgram();
            }
        }
    }

    private static void exitProgram() {
        fileHandler.sortAndSaveHexValues(); // Sort and save hex values before exiting
        System.out.println("\nProgram terminated by user. Goodbye!");
        System.exit(0);
    }

    /**
     * Waits for Button A to be pressed before proceeding.
     * Displays an error message if any keyboard input is detected.
     */
    private static void waitForButtonPressA() throws InterruptedException {
        Scanner scanner = new Scanner(System.in); // Create a Scanner to detect keyboard input

        while (true) {
            if (buttonAPressed) {
                buttonAPressed = false; // Reset the flag
                break; // Exit the loop and proceed
            } else {
                try {
                    if (System.in.available() > 0) { // Check if keyboard input is available
                        String input = scanner.nextLine().trim(); // Read the keyboard input
                        if (!input.isEmpty()) {
                            errorHandler.handleInvalidInput("You can only press A on the SwiftBot to start. Invalid input detected.");
                        }
                    }
                } catch (IOException e) {
                    // Log the error but do not terminate
                    System.err.println("Error checking for keyboard input: " + e.getMessage());
                }
            }

            // Wait for a button press
            Thread.sleep(100); // Prevents excessive CPU usage
        }

        // Do NOT close the scanner!
    }
}
