import swiftbot.*;

public class DanceMovement {
    private SwiftBotAPI swiftbot;
    private SpeedCalculator speedCalc;

    public DanceMovement(SwiftBotAPI swiftbot, SpeedCalculator speedCalc) {
        this.swiftbot = swiftbot;
        this.speedCalc = speedCalc; // Inject SpeedCalculator for dynamic speed adjustment
    }

    /**
     * Executes the dance routine based on the binary value.
     * - Reads the binary digits from right to left.
     * - Moves forward if the digit is 1.
     * - Spins in place if the digit is 0.
     * - Uses the calculated speed from SpeedCalculator for movements.
     *
     * @param binaryValue The binary value derived from the hexadecimal input.
     * @param duration    The duration of each forward movement (in milliseconds).
     * @param octalValue  The octal value used to calculate the speed.
     */
    public void executeDance(String binaryValue, int duration, int octalValue) {
        // Calculate the speed based on the octal value
        int speed = speedCalc.calculateSpeed(octalValue);

        // Read the binary digits from right to left
        for (int i = binaryValue.length() - 1; i >= 0; i--) {
            char digit = binaryValue.charAt(i);

            if (digit == '1') {
                // Move forward at the calculated speed
                swiftbot.move(speed, speed, duration); // Use dynamic speed for forward movement
            } else if (digit == '0') {
                // Spin in place at half the calculated speed (for smoother spins)
                swiftbot.move(-speed / 2, speed / 2, 500); // Adjust spin speed and duration
            }
        }
    }
}