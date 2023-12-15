package org.firstinspires.ftc.teamcode.OpModes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class TestMotorEncoder extends OpMode {
    DcMotor motor;
    double SPEED = 1;
    double FINE_TUNE_SPEED = SPEED /5;

    @Override
    public void init() {
        motor = hardwareMap.get(DcMotor.class, "motorTest");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Hardware: ", "Initialized");
    }

    @Override
    public void loop() {
        double move = -gamepad1.left_trigger * SPEED;
        move += gamepad1.right_trigger * SPEED;

        move += (gamepad1.left_bumper ? -1 : 0) * FINE_TUNE_SPEED;
        move += (gamepad1.right_bumper ? 1 : 0) * FINE_TUNE_SPEED;

        motor.setPower(move);

        telemetry.addData("MOTOR POSITION:", motor.getCurrentPosition());
    }
}