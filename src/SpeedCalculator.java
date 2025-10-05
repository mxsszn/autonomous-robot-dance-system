public class SpeedCalculator {
    private static final int MAX_SPEED = 100; // Maximum speed limit for the SwiftBot

    /**
     * Calculates the SwiftBot's speed based on the octal value.
     * If the octal value is less than 50, the speed is adjusted to octal + 50.
     * If the octal value exceeds the maximum speed, it is capped at the maximum speed.
     *
     * @param octalValue The octal value derived from the hexadecimal input.
     * @return The calculated speed for the SwiftBot.
     */
    public int calculateSpeed(int octalValue) {
        int speed = octalValue;

        // Adjust speed if the octal value is less than 50
        if (speed < 50) {
            speed += 50;
        }

        // Cap the speed at the maximum limit
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }

        return speed;
    }
}