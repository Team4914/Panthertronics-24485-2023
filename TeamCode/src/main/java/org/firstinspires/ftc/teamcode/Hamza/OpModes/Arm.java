package org.firstinspires.ftc.teamcode.Hamza.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class
Arm {
    //final static double WRIST_OPEN = 0.8;
    //final static double WRIST_CLOSED = 0.2;
    final static double WRIST_SPEED = 0.005;
    final static double CLAW_OPEN = 0.4;
    final static double CLAW_CLOSED = 0.8;

    OpMode opMode;

    DcMotor elbowMotorLeft, elbowMotorRight;
    Servo wristServo;
    Servo clawServo;

    int cnt;

    double wristPos = 0;

    public Arm(OpMode opMode) {
        this.opMode = opMode;

        elbowMotorLeft = opMode.hardwareMap.get(DcMotor.class, "elbowLeft");
        elbowMotorRight = opMode.hardwareMap.get(DcMotor.class, "elbowRight");

        wristServo = opMode.hardwareMap.get(Servo.class, "wrist");
        clawServo = opMode.hardwareMap.get(Servo.class, "claw");

        elbowMotorLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        opMode.telemetry.addData("Arm: ", "Initialized");
    }

    public void update() {
        // rotate whole arm (elbow joint motor)
        double elbowMotorPower = 0;
        elbowMotorPower -= opMode.gamepad1.left_trigger;
        elbowMotorPower += opMode.gamepad1.right_trigger;

        elbowMotorLeft.setPower(elbowMotorPower);
        elbowMotorRight.setPower(opMode.gamepad1.left_stick_y);

        opMode.telemetry.addData("Elbow Power: ", elbowMotorPower);

        // Wrist
        wristPos += (opMode.gamepad1.left_bumper? -1 : 0) * WRIST_SPEED;
        wristPos += (opMode.gamepad1.right_bumper? 1 : 0) * WRIST_SPEED;

        if (wristPos > 1) wristPos = 1;
        else if (wristPos < 0) wristPos = 0;
        else wristServo.setPosition(wristPos);

        opMode.telemetry.addData("Wrist Pos: ", wristPos);

        // Claw
        if (opMode.gamepad1.x) {
            clawServo.setPosition(CLAW_CLOSED);
            opMode.telemetry.addData("Key Pressed: ", "Claw Close (X)");
        }
        else if (opMode.gamepad1.y) {
            clawServo.setPosition(CLAW_OPEN);
            opMode.telemetry.addData("Key Pressed: ", "Claw Open (Y)");
        }

        cnt++;
        opMode.telemetry.addData("Count: ", cnt);
    }
}
