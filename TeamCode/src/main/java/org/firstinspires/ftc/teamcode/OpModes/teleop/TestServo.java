package org.firstinspires.ftc.teamcode.OpModes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class TestServo extends OpMode {
    Servo servo;
    double WRIST_SPEED = 0.01;
    double FINE_TUNE_SPEED = WRIST_SPEED /5;
    double wristPos = 0;


    @Override
    public void init() {
        servo = hardwareMap.get(Servo.class, "servoTest");

        telemetry.addData("Hardware: ", "Initialized");
    }

    @Override
    public void loop() {
        wristPos += -gamepad1.left_trigger * WRIST_SPEED;
        wristPos += gamepad1.right_trigger * WRIST_SPEED;

        wristPos += (gamepad1.left_bumper ? -1 : 0) * FINE_TUNE_SPEED;
        wristPos += (gamepad1.right_bumper ? 1 : 0) * FINE_TUNE_SPEED;

        servo.setPosition(wristPos);
        telemetry.addData("WRIST POSITION:", wristPos);
    }
}