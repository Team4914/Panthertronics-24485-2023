package org.firstinspires.ftc.teamcode.OpModes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous
public class WallRamBoardBlue extends LinearOpMode{
    DcMotor RFMotor;
    DcMotor LFMotor;
    DcMotor RBMotor;
    DcMotor LBMotor;

    final static int TICKS_PER_ROTATION = 1440;
    final static int TIRE_DIAMETER_INCHES = 4;
    @Override
    public void runOpMode() throws InterruptedException{
        telemetry.addData("Testing", "Success");

        RFMotor = hardwareMap.get(DcMotor.class, "right_front_drive");
        LFMotor = hardwareMap.get(DcMotor.class, "left_front_drive");
        RBMotor = hardwareMap.get(DcMotor.class, "right_back_drive");
        LBMotor = hardwareMap.get(DcMotor.class, "left_back_drive");

        RFMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        RBMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Auto: ", "Initialized");

        waitForStart();
        RFMotor.setPower(1);
        RBMotor.setPower(-1);
        LFMotor.setPower(-1);
        LBMotor.setPower(1);
        Thread.sleep(1500);
        resetMotors();
    }

    void resetMotors() {
        RFMotor.setPower(0);
        RBMotor.setPower(0);
        LFMotor.setPower(0);
        LBMotor.setPower(0);
    }
}
