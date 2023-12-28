package org.firstinspires.ftc.teamcode.parts;

public class TappedButton {
    // this is literally a 1-tick monostable circuit

    boolean wasPressed = false;

    // returns true on first loop of a press
    public boolean update(boolean input) {
        boolean result = input && !wasPressed;
        wasPressed = input;
        return result;
    }
}
