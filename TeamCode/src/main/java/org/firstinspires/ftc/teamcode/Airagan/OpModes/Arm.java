package org.firstinspires.ftc.teamcode.Airagan.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class Arm {
    final static double ELBOW_SPEED = 0.7;

    OpMode opMode;

    DcMotor elbowMotor;
    DcMotor intakeMotor;

    int cnt;

    double wristPos = 0;

    public Arm(OpMode opMode) {
        this.opMode = opMode;

        elbowMotor = opMode.hardwareMap.get(DcMotor.class, "elbow");

        intakeMotor = opMode.hardwareMap.get(DcMotor.class, "intakeMotor");
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);


        //elbowMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        opMode.telemetry.addData("Arm: ", "Initialized");
    }

    public void update() {
        // rotate whole arm (elbow joint motor)
        double elbowMotorPower = 0;
        elbowMotorPower -= opMode.gamepad1.left_trigger;
        elbowMotorPower += opMode.gamepad1.right_trigger;

        elbowMotor.setPower(elbowMotorPower * ELBOW_SPEED);

        opMode.telemetry.addData("Elbow Power: ", elbowMotorPower);

        if (opMode.gamepad1.a) {
            intakeMotor.setPower(1);
        }
        else {
            intakeMotor.setPower(0);
        }
    }
}
