package org.firstinspires.ftc.teamcode.parts;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class Arm {
    //final static double WRIST_OPEN = 0.8;
    //final static double WRIST_CLOSED = 0.2;
    final static double CLAW_OPEN = 0.5;
    final static double CLAW_CLOSED = 1;

    OpMode opMode;
    final static double WRIST_SPEED = 0.001;
    public DcMotor elbowMotorLeft, elbowMotorRight;
    public Servo clawLeft, clawRight, wrist;

    public int cycleLeft = -1, cycleRight = -1;
    public boolean leftPressed = false, rightPressed = false;
    double wristPos = 0;

    // Elbow Positions

    public Arm(OpMode opMode) {
        this.opMode = opMode;

        elbowMotorLeft = opMode.hardwareMap.get(DcMotor.class, "elbowLeft");
        elbowMotorRight = opMode.hardwareMap.get(DcMotor.class, "elbowRight");

        clawLeft = opMode.hardwareMap.get(Servo.class, "clawLeft");
        clawRight = opMode.hardwareMap.get(Servo.class, "clawRight");
        wrist = opMode.hardwareMap.get(Servo.class, "wrist");

        elbowMotorLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        opMode.telemetry.addData("Arm: ", "Initialized");
    }

    public void update() {
        // rotate whole arm (elbow joint motor)
        double elbowMotorPower = 0;
        elbowMotorPower -= opMode.gamepad1.left_trigger;
        elbowMotorPower += opMode.gamepad1.right_trigger;

        elbowMotorLeft.setPower(elbowMotorPower);
        elbowMotorRight.setPower(elbowMotorPower);

        opMode.telemetry.addData("Elbow Power: ", elbowMotorPower);

        // Wrist
        wristPos += (opMode.gamepad1.left_bumper? -1 : 0) * WRIST_SPEED;
        wristPos += (opMode.gamepad1.right_bumper? 1 : 0) * WRIST_SPEED;

        wrist.setPosition(wristPos);

        // Claw
        if (opMode.gamepad1.x && !leftPressed) {
            leftPressed = true;
            cycleLeft = (cycleLeft + 1) % 2;

            if (cycleLeft == 0) {
                clawLeft.setPosition(CLAW_CLOSED);
                opMode.telemetry.addData("Claw Left", "CLOSED");
            }
            else {
                clawLeft.setPosition(CLAW_OPEN);
                opMode.telemetry.addData("Claw Left", "OPEN");
            }
        }
        else {
            leftPressed = false;
        }

        if (opMode.gamepad1.b && !rightPressed) {
            rightPressed = true;
            cycleRight = (cycleRight + 1) % 2;

            if (cycleRight == 0) {
                clawRight.setPosition(CLAW_CLOSED);
                opMode.telemetry.addData("Claw Right", "Closed");
            }
            else {
                clawRight.setPosition(CLAW_OPEN);
                opMode.telemetry.addData("Claw Right", "OPEN");
            }
        }
        else {
            rightPressed = false;
        }


        opMode.telemetry.addData("CycleLeft", cycleLeft);
        opMode.telemetry.addData("CycleRight", cycleRight);
    }
}
