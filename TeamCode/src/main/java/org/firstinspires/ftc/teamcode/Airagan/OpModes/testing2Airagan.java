package org.firstinspires.ftc.teamcode.Airagan.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

// Shared Import
import org.firstinspires.ftc.teamcode.shared.*;

@TeleOp
public class testing2Airagan extends OpMode {
    Servo servo;
    DcMotor motor;
    @Override
    public void init() {
        //servo = hardwareMap.get(Servo.class, "a");
        motor = hardwareMap.get(DcMotor.class, "b");

        telemetry.addData("Hardware: ", "Initialized");
    }

    // Motor Testing
    @Override
    public void loop() {
        if (gamepad1.dpad_up) {
            motor.setPower(1);
            //servo.setPosition(0);
        }
        else if (gamepad1.dpad_down) {
            motor.setPower(-1);
            //servo.setPosition(1);
        }
        else {
            motor.setPower(0);
        }
    }
}