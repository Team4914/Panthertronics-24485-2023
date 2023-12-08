package org.firstinspires.ftc.teamcode.parts;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class Hanger {
    public static double HANG_SERVO_DOWN = 0;
    public static double HANG_SERVO_UP = 1;

    OpMode opmode;

    Servo liftServo;
    DcMotor hangMotor;

    public Hanger(OpMode opmode) {
        this.opmode = opmode;

        //Servo to lift up the hook
        liftServo = opmode.hardwareMap.get(Servo.class, "liftServo");
        //Motor to pull up the robot
        hangMotor = opmode.hardwareMap.get(DcMotor.class, "hangMotor");
    }

    // Separating update functions for each component for simplify debugging;
    private void updateServo() {
        if (opmode.gamepad1.dpad_left) {
            liftServo.setPosition(HANG_SERVO_DOWN);
        }
        else if (opmode.gamepad1.dpad_right) {
            liftServo.setPosition(HANG_SERVO_UP);
        }
    }

    private void updateMotor() {
        if (opmode.gamepad1.dpad_up) {
            hangMotor.setPower(1);
        }
        else if (opmode.gamepad1.dpad_down) {
            hangMotor.setPower(-1);
        }
        else {
            hangMotor.setPower(0);
        }
    }

    public void update() {
        updateServo();
        updateMotor();
    }
}
