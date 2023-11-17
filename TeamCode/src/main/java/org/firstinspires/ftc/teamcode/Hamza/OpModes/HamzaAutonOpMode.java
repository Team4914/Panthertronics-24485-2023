package org.firstinspires.ftc.teamcode.Hamza.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous
public class HamzaAutonOpMode extends LinearOpMode {
    DcMotor RFMotor;
    DcMotor LFMotor;
    DcMotor RBMotor;
    DcMotor LBMotor;

    final static int TICKS_PER_ROTATION = 1440;
    final static int TIRE_DIAMETER_INCHES = 4;
    @Override
    public void runOpMode() throws InterruptedException {
        RFMotor = hardwareMap.get(DcMotor.class, "right_front_drive");
        LFMotor = hardwareMap.get(DcMotor.class, "left_front_drive");
        RBMotor = hardwareMap.get(DcMotor.class, "right_back_drive");
        LBMotor = hardwareMap.get(DcMotor.class, "left_back_drive");

        RFMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        RBMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        LFMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        LBMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        initalizeMotor(RFMotor);
        initalizeMotor(RBMotor);
        initalizeMotor(LFMotor);
        initalizeMotor(LBMotor);

        telemetry.addData("Auto: ", "Initialized");

        waitForStart();

        int dist = 4*12;
        double tireCircumference = Math.PI * TIRE_DIAMETER_INCHES;
        double rotations = dist / tireCircumference;
        int ticks = (int) (rotations * TICKS_PER_ROTATION);

        RFMotor.setTargetPosition(ticks);
        RBMotor.setTargetPosition(ticks);
        LFMotor.setTargetPosition(ticks);
        LBMotor.setTargetPosition(ticks);

        while (RFMotor.getCurrentPosition() < ticks) {
            opModeIsActive();
        };
    }

    void initalizeMotor(DcMotor motor) {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setPower(0);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}
