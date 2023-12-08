package org.firstinspires.ftc.teamcode.parts;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Launcher {

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
        else if (opMode.gamepad1.b) {
            launchServo.setPosition(1);
        }
    }
}
