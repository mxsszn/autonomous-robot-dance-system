public class NumberConverter {

    /**
     * Converts a hexadecimal string to its decimal equivalent manually.
     * - Iterates through each character of the hex string.
     * - Converts each character to its decimal value and calculates the total.
     *
     * @param hex The hexadecimal string to convert.
     * @return The decimal equivalent of the hexadecimal value.
     */
    public int hexToDecimal(String hex) {
        int decimal = 0;
        int length = hex.length();
        for (int i = 0; i < length; i++) {
            char ch = hex.charAt(i);
            int digit = hexCharToDecimal(ch); // Convert hex character to decimal
            decimal = decimal * 16 + digit; // Accumulate the decimal value
        }
        return decimal;
    }

    /**
     * Converts a hexadecimal character to its decimal equivalent.
     * - Handles digits (0-9) and letters (A-F, case-insensitive).
     *
     * @param ch The hexadecimal character to convert.
     * @return The decimal value of the character.
     * @throws IllegalArgumentException if the character is not a valid hex digit.
     */
    private int hexCharToDecimal(char ch) {
        if (ch >= '0' && ch <= '9') {
            return ch - '0'; // Convert digit characters to their numeric value
        } else if (ch >= 'A' && ch <= 'F') {
            return 10 + (ch - 'A'); // Convert uppercase A-F to 10-15
        } else if (ch >= 'a' && ch <= 'f') {
            return 10 + (ch - 'a'); // Convert lowercase a-f to 10-15
        } else {
            throw new IllegalArgumentException("Invalid hex character: " + ch);
        }
    }

    /**
     * Converts a hexadecimal string to its octal equivalent manually.
     * - First converts the hex string to decimal, then converts decimal to octal.
     *
     * @param hex The hexadecimal string to convert.
     * @return The octal equivalent of the hexadecimal value.
     */
    public int hexToOctal(String hex) {
        int decimal = hexToDecimal(hex); // Convert hex to decimal first
        return decimalToOctal(decimal); // Convert decimal to octal
    }

    /**
     * Converts a decimal number to its octal equivalent manually.
     * - Uses repeated division by 8 to calculate the octal digits.
     *
     * @param decimal The decimal number to convert.
     * @return The octal equivalent of the decimal value.
     */
    private int decimalToOctal(int decimal) {
        int octal = 0;
        int place = 1; // Represents the current place value (1, 8, 64, etc.)
        while (decimal > 0) {
            int remainder = decimal % 8; // Get the remainder (current octal digit)
            octal += remainder * place; // Accumulate the octal value
            decimal /= 8; // Move to the next place value
            place *= 10; // Increment the place value
        }
        return octal;
    }

    /**
     * Converts a hexadecimal string to its binary equivalent manually.
     * - First converts the hex string to decimal, then converts decimal to binary.
     *
     * @param hex The hexadecimal string to convert.
     * @return The binary equivalent of the hexadecimal value as a string.
     */
    public String hexToBinary(String hex) {
        int decimal = hexToDecimal(hex); // Convert hex to decimal first
        return decimalToBinary(decimal); // Convert decimal to binary
    }

    /**
     * Converts a decimal number to its binary equivalent manually.
     * - Uses repeated division by 2 to calculate the binary digits.
     *
     * @param decimal The decimal number to convert.
     * @return The binary equivalent of the decimal value as a string.
     */
    private String decimalToBinary(int decimal) {
        if (decimal == 0) return "0"; // Handle edge case
        StringBuilder binary = new StringBuilder();
        while (decimal > 0) {
            int remainder = decimal % 2; // Get the remainder (current binary digit)
            binary.insert(0, remainder); // Prepend the digit to the binary string
            decimal /= 2; // Move to the next place value
        }
        return binary.toString();
    }
}