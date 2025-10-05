import swiftbot.*;

public class DemoDance {
    private SwiftBotAPI swiftbot;
    private SpeedCalculator speedCalc;
    private LEDController ledControl;

    public DemoDance(SwiftBotAPI swiftbot, SpeedCalculator speedCalc, LEDController ledControl) {
        this.swiftbot = swiftbot;
        this.speedCalc = speedCalc;
        this.ledControl = ledControl;
    }

    /**
     * Executes a hardcoded demo dance routine.
     */
    public void executeDemoDance() throws InterruptedException {
        System.out.println("\nStarting demo dance...");
        Thread.sleep(2000); // Pause for 2 seconds

        // Hardcoded dance movements
        String[] demoMoves = {
            "1010", // Forward, Spin, Forward, Spin
            "0101", // Spin, Forward, Spin, Forward
            "1111", // Forward, Forward, Forward, Forward
            "0000"  // Spin, Spin, Spin, Spin
        };

        // Execute each move
        DanceMovement dancer = new DanceMovement(swiftbot, speedCalc);
        for (String move : demoMoves) {
            ledControl.setLEDColor(255); // Set LED to a bright color for the demo
            dancer.executeDance(move, 1000, 100); // Execute each move with a speed of 100
            ledControl.turnOffLED();
        }

        System.out.println("\nDemo ended!");
    }
}