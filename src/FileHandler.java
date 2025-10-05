import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileHandler {
    private static final String LOG_FILE_PATH = "swiftbot_dance_log.txt"; // File where QR data is stored
    private static List<String> allHexValues = new ArrayList<>(); // Stores all valid hex values scanned

    /**
     * Saves the QR code content to the log file and extracts valid hexadecimal values.
     * - Also adds valid hex values to a global list for sorting when the program exits.
     *
     * @param qrContent The raw QR code data containing hex values.
     */
    public void saveQRCodeToLog(String qrContent) {
        try (FileWriter writer = new FileWriter(LOG_FILE_PATH, true)) { // Open log file in append mode
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write(timestamp + " - " + qrContent + "\n"); // Log timestamp and QR data

            // Extract and store valid hex values (1-2 digit hex numbers only)
            String[] parts = qrContent.split(":");
            for (String part : parts) {
                if (part.matches("^[0-9A-Fa-f]{1,2}$")) { // Validate hex format
                    allHexValues.add(part.toUpperCase()); // Convert to uppercase and store
                }
            }

            System.out.println("QR code saved to: " + getLogFilePath());
        } catch (IOException e) {
            System.out.println("Error saving QR code: " + e.getMessage());
        }
    }

    /**
     * Sorts all stored hex values in ascending order and writes them to the log file.
     * - Called when the user chooses to exit the program.
     */
    public void sortAndSaveHexValues() {
        if (allHexValues.isEmpty()) {
            System.out.println("No hex values to save.");
            return;
        }

        Collections.sort(allHexValues); // Sort the hex values alphabetically

        try (FileWriter writer = new FileWriter(LOG_FILE_PATH, true)) { // Append sorted values to log file
            writer.write("\nSorted Hex Values:\n");
            for (String hex : allHexValues) {
                writer.write(hex + "\n"); // Write each sorted hex value
            }
            System.out.println("Sorted hex values saved to: " + getLogFilePath());
        } catch (IOException e) {
            System.out.println("Error saving sorted hex values: " + e.getMessage());
        }
    }

    /**
     * Retrieves the last scanned QR code from the stored list.
     * - Returns the most recent valid QR code data.
     *
     * @return The last valid hex value string or null if no values exist.
     */
    public String getLastQRCode() {
        if (allHexValues.isEmpty()) {
            return null; // No QR codes stored
        }
        return allHexValues.get(allHexValues.size() - 1); // Return the last entered hex value
    }

    /**
     * Returns the absolute file path of the log file.
     *
     * @return The full path as a string.
     */
    public String getLogFilePath() {
        return Paths.get(LOG_FILE_PATH).toAbsolutePath().toString();
    }

    /**
     * Reads and displays the contents of the log file.
     */
    public void displayLogFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH))) {
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
