package org.firstinspires.ftc.teamcode.OpModes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

// Shared Import
import org.firstinspires.ftc.teamcode.parts.*;

@TeleOp
public class testing2Airagan extends OpMode {
    Servo servo;
    DcMotor motor;
    int power = 0;
    @Override
    public void init() {
        //servo = hardwareMap.get(Servo.class, "a");
        motor = hardwareMap.get(DcMotor.class, "b");

        telemetry.addData("Hardware: ", "Initialized");
    }

    // Motor Testing
    @Override
    public void loop() {
        if (gamepad1.a) {
            power = 1;
            telemetry.addData("Key", "a");
            //servo.setPosition(0);
        }
        else if (gamepad1.b) {
            power = -1;
            //servo.setPosition(1);
            telemetry.addData("Key", "b");
        }
        else {
            power = 0;
        }
        motor.setPower(power);
        telemetry.addData("Power", power);
    }
}