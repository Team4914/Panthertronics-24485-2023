package org.firstinspires.ftc.teamcode.Hamza.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorImpl;

public class Roller {
    OpMode opmode;
    DcMotor intakeMotor;
    public Roller(OpMode opmode) {
        // Roller Intake;

        intakeMotor = opmode.hardwareMap.get(DcMotor.class, "intakeMotor");
    }

    public void update() {
        if (opmode.gamepad1.a) {
            intakeMotor.setPower(1);
        }
        else {
            intakeMotor.setPower(0);
        }
    }
}
