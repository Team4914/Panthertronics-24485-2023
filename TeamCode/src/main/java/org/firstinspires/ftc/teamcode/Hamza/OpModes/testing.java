package org.firstinspires.ftc.teamcode.Hamza.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
@TeleOp
public class testing extends OpMode {
    DcMotor motor;
    Servo servo;
    @Override
    public void init() {
        motor = hardwareMap.get(DcMotor.class, "a");
        servo = hardwareMap.get(Servo.class, "b");

        telemetry.addData("Hardware: ", "Initialized");
      }

    @Override
    public void loop() {
        if (gamepad1.left_stick_y > 0) {
            motor.setPower(1);
        } else if (gamepad1.left_stick_y < 0) {
            motor.setPower(-1);
        } else {
            motor.setPower(0);
        }

        if(gamepad1.y) {
            // move to 0 degrees.
            telemetry.addData("Key", "y");
            servo.setPosition(0);
        } else if (gamepad1.x || gamepad1.b) {
            // move to 90 degrees.
            telemetry.addData("Key", "x");
            servo.setPosition(0.5);
        } else if (gamepad1.a) {
            // move to 180 degrees.
            telemetry.addData("Key", "a");
            servo.setPosition(1);
        }
    }


}