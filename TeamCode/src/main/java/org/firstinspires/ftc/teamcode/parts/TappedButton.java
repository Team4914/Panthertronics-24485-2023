package org.firstinspires.ftc.teamcode.parts;

public class TappedButton {
    boolean wasPressed = false;

    // returns true on first loop of a press
    public boolean update(boolean input) {
        boolean result = input && !wasPressed;
        wasPressed = input;
        return result;
    }
}
