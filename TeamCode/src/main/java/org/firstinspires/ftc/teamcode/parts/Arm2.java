package org.firstinspires.ftc.teamcode.parts;

import
        com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class Arm2 {
    final static double CLAW_OPEN = 0.67;
    final static double CLAW_CLOSED = 0.45;

    final static double CLAW_OPEN_RIGHT = 0.1828; // Temp fixes
    final static double CLAW_CLOSED_RIGHT = 0;

    OpMode opMode;
    public DcMotor elbowMotorLeft, elbowMotorRight;
    private Servo clawLeft, clawRight, wrist;

    double wristPos = 0.5;
    private int cycleLeft = -1, cycleRight = -1;
    private boolean leftPressed = false, rightPressed = false;

    // Elbow Positions
    public static int ELBOW_GROUND = -1530;
    public static int ELBOW_BOARD = ELBOW_GROUND + 350;
    public static int ELBOW_STORAGE = 0;
    public static int ELBOW_SHIFT = 0;
    

    // Wrist Positions
    public static double WRIST_GROUND = 0.64;
    public static double WRIST_BOARD = 0.645;
    public static double WRIST_STORAGE = 0.03;
    // States
    public static int STORAGE_STATE = 0;
    public static int BOARD_STATE = 1;
    public static int GROUND_STATE = 2;

    static int[] ELBOW_STATE_POS = { ELBOW_STORAGE, ELBOW_BOARD, ELBOW_GROUND };
    static double[] WRIST_STATE_POS = { WRIST_STORAGE, WRIST_BOARD, WRIST_GROUND };

    //int lastElbowTargetPos = 0;
    //int curPos = 0;
    //int curMove = 0;
    //int move = 0;

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
        clawLeft.setDirection(Servo.Direction.REVERSE);

        opMode.telemetry.addData("Arm: ", "Initialized");
    }

    public void update() {
        if (opMode.gamepad1.left_trigger > 0) {
            setState(Arm2.GROUND_STATE);
        }

        else if (opMode.gamepad1.right_trigger > 0) {
            setState(Arm2.BOARD_STATE);
        }

        // wristPos += (opMode.gamepad1.left_bumper? -1 : 0) * WRIST_SPEED;
        // wristPos += (opMode.gamepad1.right_bumper? 1 : 0) * WRIST_SPEED;
        //
        // wrist.setPosition(wristPos);

        //opMode.telemetry.addData("Moving Elbow From: ", curPos);
        //opMode.telemetry.addData("Moving Elbow To: ", curPos + move);

        // Claw
        if (opMode.gamepad1.x && !leftPressed) {
            leftPressed = true;
            cycleLeft = (cycleLeft + 1) % 2;

            if (cycleLeft == 0) {
                closeClawLeft();
                opMode.telemetry.addData("Claw Left", "CLOSED");
            }
            else {
                openClawLeft();
                opMode.telemetry.addData("Claw Left", "OPEN");
            }
        }
        if (!opMode.gamepad1.x) {
            leftPressed = false;
        }

        if (opMode.gamepad1.b && !rightPressed) {
            rightPressed = true;
            cycleRight = (cycleRight + 1) % 2;

            if (cycleRight == 0) {
                closeClawRight();
                opMode.telemetry.addData("Claw Right", "Closed");
            }
            else {
                openClawRight();
                opMode.telemetry.addData("Claw Right", "OPEN");
            }
        }

        if (!opMode.gamepad1.b) {
            rightPressed = false;
        }


        opMode.telemetry.addData("CycleLeft", cycleLeft);
        opMode.telemetry.addData("CycleRight", cycleRight);
    }

    public void reset() {
        setElbowTargetPos(-ELBOW_GROUND);
        //moveElbow(-ELBOW_GROUND); // Hardcoded value to be called in seperate opmode
        wrist.setPosition(WRIST_STORAGE);
    }

    public void adjust() {

    }

    public void setElbowTargetPos(int pos) {
        elbowMotorLeft.setTargetPosition(pos);
        elbowMotorRight.setTargetPosition(pos);
        elbowMotorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elbowMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elbowMotorLeft.setPower(0.5);
        elbowMotorRight.setPower(0.5);
    }
//    public void moveElbow(int dist) {
//        elbowMotorLeft.setTargetPosition(dist);
//        elbowMotorRight.setTargetPosition(dist);
//        opMode.telemetry.addData("Moving Elbow By: ", dist);
//        elbowMotorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        elbowMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        elbowMotorLeft.setPower(0.5);
//        elbowMotorRight.setPower(0.5);
//
//        if (!elbowMotorLeft.isBusy()) {
//            elbowMotorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            elbowMotorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        }
//    }

//    public void setElbowTargetPos(int pos) {
//        curMove = elbowMotorLeft.getCurrentPosition();
//        curPos = lastElbowTargetPos + curMove;
//
//        move = pos - curPos;
//        moveElbow(move);
//    }
//
////    public void setWristPosition(double pos) {
////        wrist.setPosition(pos);
////    }
//
    public void setState(int state) {
        if (state == STORAGE_STATE) closeClawRight();

        setElbowTargetPos(ELBOW_STATE_POS[state]);
        wrist.setPosition(WRIST_STATE_POS[state]);
    }

    public void openClawLeft() {
        clawLeft.setPosition(CLAW_OPEN);
    }
    public void closeClawLeft() {
        clawLeft.setPosition(CLAW_CLOSED);
    }
    public void openClawRight() {
        clawRight.setPosition(CLAW_OPEN_RIGHT);
    }
    public void closeClawRight () {
        clawRight.setPosition(CLAW_CLOSED_RIGHT);
    }
}
