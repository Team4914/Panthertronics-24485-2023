package org.firstinspires.ftc.teamcode.parts;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Launcher {
    public static final double LAUNCH_OPEN = 0;
    public static final double LAUNCH_CLOSE = 1;

    OpMode opMode;

    Servo launchServo;

    public Launcher(OpMode opMode) {
        this.opMode = opMode;

        launchServo = opMode.hardwareMap.get(Servo.class, "launchServo");
    }
    public void update() {
        if (opMode.gamepad1.y) {
            launchServo.setPosition(0);
        }
        else if (opMode.gamepad1.a) {
            launchServo.setPosition(1);
        }
        else launchServo.setPosition(0.5);
    }
}
