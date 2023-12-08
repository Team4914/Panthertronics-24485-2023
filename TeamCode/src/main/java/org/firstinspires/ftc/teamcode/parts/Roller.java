package org.firstinspires.ftc.teamcode.parts;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Roller {

    OpMode opMode;
    DcMotor intakeMotor;

    public Roller(OpMode opMode) {
        this.opMode = opMode;
        intakeMotor = opMode.hardwareMap.get(DcMotor.class, "intakeMotor");
    }

    public void update() {
        if (opMode.gamepad1.a) {
            intakeMotor.setPower(1);
        }
        else if (opMode.gamepad1.b) {
            intakeMotor.setPower(-1);
        }
        else {
            intakeMotor.setPower(0);
        }
    }
}
