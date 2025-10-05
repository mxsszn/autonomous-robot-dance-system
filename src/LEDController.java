import swiftbot.*;

public class LEDController {
    private SwiftBotAPI swiftbot;

    public LEDController(SwiftBotAPI swiftbot) {
        this.swiftbot = swiftbot;
    }

    /**
     * Sets the LED underlights based on the decimal value of the hexadecimal input.
     * - Red component: Decimal value.
     * - Green component: (Decimal % 80) * 3.
     * - Blue component: The greater of the red or green values.
     *
     * @param decimalValue The decimal value derived from the hexadecimal input.
     */
    public void setLEDColor(int decimalValue) {
        // Calculate the RGB components
        int red = decimalValue;
        int green = (decimalValue % 80) * 3;
        int blue = Math.max(red, green);

        // Ensure RGB values are within the valid range (0-255)
        red = Math.min(red, 255);
        green = Math.min(green, 255);
        blue = Math.min(blue, 255);

        // Set the LED underlights
        int[] rgb = {red, green, blue};
        swiftbot.fillUnderlights(rgb);
    }

    /**
     * Turns off all LED underlights.
     */
    public void turnOffLED() {
        swiftbot.disableUnderlights();
    }
}