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

    // State
    public enum State {
        GROUND(-1550, 0.64),
        BOARD(GROUND.ELBOW_POS + 350, 0.654),
        STORAGE(0, 0.03);

        public final int ELBOW_POS;
        public final double WRIST_POS;

        private State(int elbowPos, double wristPos) {
            this.ELBOW_POS = elbowPos;
            this.WRIST_POS = wristPos;
        }
    }
    State state = State.STORAGE;

    //double wristPos = 0.5;
    private int cycleLeft = -1, cycleRight = -1;
    private TappedButton leftClawBut = new TappedButton();
    private TappedButton rightClawBut = new TappedButton();

    double elbowShift = 0;

    public Arm2(OpMode opMode) {
        this.opMode = opMode;

        elbowMotorLeft = opMode.hardwareMap.get(DcMotor.class, "elbowLeft");
        elbowMotorRight = opMode.hardwareMap.get(DcMotor.class, "elbowRight");
        wrist = opMode.hardwareMap.get(Servo.class, "wrist");

        elbowMotorLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        // Reset the motor encoder so that it reads zero ticks
        elbowMotorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbowMotorLeft.setTargetPosition(State.STORAGE.ELBOW_POS);
        // Turn the motor back on, required if you use STOP_AND_RESET_ENCODER
        elbowMotorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Reset the motor encoder so that it reads zero ticks
        elbowMotorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbowMotorRight.setTargetPosition(State.STORAGE.ELBOW_POS);
        // Turn the motor back on, required if you use STOP_AND_RESET_ENCODER
        elbowMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        clawLeft = opMode.hardwareMap.get(Servo.class, "clawLeft");
        clawRight = opMode.hardwareMap.get(Servo.class, "clawRight");
        clawLeft.setDirection(Servo.Direction.REVERSE);

        opMode.telemetry.addData("Arm: ", "Initialized");
    }

    public void update() {
        if (opMode.gamepad1.left_trigger > 0) {
            setState(State.GROUND);
        }

        else if (opMode.gamepad1.right_trigger > 0) {
            setState(State.BOARD);
        }

        // wristPos += (opMode.gamepad1.left_bumper? -1 : 0) * WRIST_SPEED;
        // wristPos += (opMode.gamepad1.right_bumper? 1 : 0) * WRIST_SPEED;
        //
        // wrist.setPosition(wristPos);

        //opMode.telemetry.addData("Moving Elbow From: ", curPos);
        //opMode.telemetry.addData("Moving Elbow To: ", curPos + move);

        // Claw
        if (leftClawBut.update(opMode.gamepad1.x)) {
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

        if (rightClawBut.update(opMode.gamepad1.b)) {
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

        opMode.telemetry.addData("cycleLeft", cycleLeft);
        opMode.telemetry.addData("cycleRight", cycleRight);
        opMode.telemetry.addData("elbowShift", elbowShift);
    }

    public void reset() {
        setElbowTargetPos(-State.GROUND.ELBOW_POS);
        //moveElbow(-ELBOW_GROUND); // Hardcoded value to be called in seperate opmode
        wrist.setPosition(State.STORAGE.WRIST_POS);
    }

    public void adjust() {
        final double ADJUST_SPEED = 0.25;

        if (state != State.GROUND) return;
        if (!opMode.gamepad1.left_bumper && !opMode.gamepad1.right_bumper) return;

        elbowShift += (opMode.gamepad1.left_bumper ? -1 : 0) * ADJUST_SPEED;
        elbowShift += (opMode.gamepad1.right_bumper ? 1 : 0) * ADJUST_SPEED;

        setState(State.GROUND);
    }
    /*
    public void adjust() {
        final double ADJUST_SPEED = 0.05;

        if (state != ELBOW_GROUND) return;
        if (!opMode.gamepad1.left_bumper && !opMode.gamepad1.right_bumper) return;

        elbowMotorLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        elbowMotorRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        elbowMotorLeft.setPower((opMode.gamepad1.left_bumper ? -1 : 0) * ADJUST_SPEED);
        elbowMotorLeft.setPower((opMode.gamepad1.right_bumper ? 1 : 0) * ADJUST_SPEED);
        elbowMotorRight.setPower((opMode.gamepad1.left_bumper ? -1 : 0) * ADJUST_SPEED);
        elbowMotorRight.setPower((opMode.gamepad1.right_bumper ? 1 : 0) * ADJUST_SPEED);

        ELBOW_SHIFT = elbowMotorLeft.getCurrentPosition() - ELBOW_GROUND;
        ELBOW_GROUND += ELBOW_SHIFT;
        ELBOW_BOARD += ELBOW_SHIFT;
        ELBOW_STORAGE += ELBOW_SHIFT;

        elbowMotorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elbowMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

     */

    public void setElbowTargetPos(int pos) {
        elbowMotorLeft.setTargetPosition(pos);
        elbowMotorRight.setTargetPosition(pos);
        elbowMotorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elbowMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elbowMotorLeft.setPower(0.7);
        elbowMotorRight.setPower(0.7);
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
    public void setState(State newState) {
        state = newState;

        if (state == State.STORAGE) {
            closeClawLeft();
            closeClawRight();
        }

        setElbowTargetPos(state.ELBOW_POS + (int) elbowShift);
        wrist.setPosition(state.WRIST_POS);
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
