package org.firstinspires.ftc.teamcode.hamzaopmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class MecanumDrive {
    OpMode opMode;

    DcMotor RFMotor;
    DcMotor LFMotor;
    DcMotor RBMotor;
    DcMotor LBMotor;
    public MecanumDrive(OpMode opMode) {
        this.opMode = opMode;

        RFMotor = opMode.hardwareMap.get(DcMotor.class, "right_front_drive");
        LFMotor = opMode.hardwareMap.get(DcMotor.class, "left_front_drive");
        RBMotor = opMode.hardwareMap.get(DcMotor.class, "right_back_drive");
        LBMotor = opMode.hardwareMap.get(DcMotor.class, "left_back_drive");

        RFMotor.setDirection(DcMotorSimple.Direction.REVERSE); // actually right front (good)
        RBMotor.setDirection(DcMotorSimple.Direction.REVERSE); // actually left back (good)
        LFMotor.setDirection(DcMotorSimple.Direction.FORWARD); // actually right back (good)
        LBMotor.setDirection(DcMotorSimple.Direction.FORWARD); // actually left front

        opMode.telemetry.addData("Mecanum Drive: ", "Initialized");
    }

    public void update() {
        double vertical; // Front-back motion
        double horizontal; // Left-right motion
        double pivot; // Turn angle

        /*Depending on what motors are flipped, vertical must be negative or positive.
         * Test with robot, and change to positive if necessary*/
        vertical = -opMode.gamepad1.left_stick_y;
        horizontal = opMode.gamepad1.left_stick_x;
        pivot = opMode.gamepad1.right_stick_x;

        // Diagonally Alternate Motors must be the same
        /* If robot does not work properly, set the vertical values for RB and LF to positive*/
        RFMotor.setPower(-pivot + (vertical - horizontal)); // good
        RBMotor.setPower(-pivot + (vertical + horizontal)); // good
        LFMotor.setPower(pivot + (vertical + horizontal)); // good
        LBMotor.setPower(pivot + (vertical - horizontal)); // good
    }
}
