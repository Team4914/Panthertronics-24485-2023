package org.firstinspires.ftc.teamcode.parts;

import
        com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class Arm2 {
    final static double CLAW_OPEN = 0.5;
    final static double CLAW_CLOSED = 1;

    OpMode opMode;
    public DcMotor elbowMotorLeft, elbowMotorRight;
    public Servo clawLeft, clawRight, wrist;

    public int cycleLeft = -1, cycleRight = -1;
    public boolean leftPressed = false, rightPressed = false;

    // Elbow Positions
    public static int ELBOW_GROUND = -200;
    public static int ELBOW_BOARD = -100;
    public static int ELBOW_STORAGE = 0;

    // Wrist Positions
    public static double WRIST_GROUND = 0.5;
    public static double WRIST_BOARD = 0.4;
    public static double WRIST_STORAGE = 0.8;

    int lastElbowTargetPos = 0;

    public Arm2(OpMode opMode) {
        this.opMode = opMode;

        elbowMotorLeft = opMode.hardwareMap.get(DcMotor.class, "elbowLeft");
        elbowMotorRight = opMode.hardwareMap.get(DcMotor.class, "elbowRight");
        wrist = opMode.hardwareMap.get(Servo.class, "wrist");

        elbowMotorLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        // Reset the motor encoder so that it reads zero ticks
        elbowMotorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbowMotorLeft.setTargetPosition(ELBOW_STORAGE);
        // Turn the motor back on, required if you use STOP_AND_RESET_ENCODER
        elbowMotorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Reset the motor encoder so that it reads zero ticks
        elbowMotorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbowMotorRight.setTargetPosition(ELBOW_STORAGE);
        // Turn the motor back on, required if you use STOP_AND_RESET_ENCODER
        elbowMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        clawLeft = opMode.hardwareMap.get(Servo.class, "clawLeft");
        clawRight = opMode.hardwareMap.get(Servo.class, "clawRight");

        opMode.telemetry.addData("Arm: ", "Initialized");
    }

    public void update() {
        if (opMode.gamepad1.left_trigger > 0) {
            setElbowTargetPos(ELBOW_GROUND);
            wrist.setPosition(WRIST_GROUND);
        }

        else if (opMode.gamepad1.right_trigger > 0) {
            setElbowTargetPos(ELBOW_BOARD);
            wrist.setPosition(WRIST_BOARD);
        }

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

    public void reset() {
        moveElbow(-ELBOW_GROUND); // Hardcoded value to be called in seperate opmode
        wrist.setPosition(WRIST_STORAGE);
    }

    public void moveElbow(int dist) {
        elbowMotorLeft.setTargetPosition(dist);
        elbowMotorRight.setTargetPosition(dist);
        opMode.telemetry.addData("Moving Elbow By: ", dist);
        elbowMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elbowMotorLeft.setPower(0.5);
        elbowMotorRight.setPower(0.5);

        if (!elbowMotorLeft.isBusy()) {
            elbowMotorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            elbowMotorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }

    public void setElbowTargetPos(int pos) {
        int curMove = elbowMotorLeft.getCurrentPosition();
        int curPos = lastElbowTargetPos + curMove;

        int move = pos - curPos;
        opMode.telemetry.addData("Moving Elbow From: ", curPos);
        opMode.telemetry.addData("Moving Elbow To: ", pos);
        moveElbow(move);
    }
}
