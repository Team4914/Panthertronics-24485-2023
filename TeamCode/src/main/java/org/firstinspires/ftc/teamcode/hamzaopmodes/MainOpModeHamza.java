package org.firstinspires.ftc.teamcode.hamzaopmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class MainOpModeHamza extends OpMode {
    MecanumDrive mecanumDrive;
    Arm arm;

    DcMotor hangRopeMotor;
    Servo hangArmServo;


    @Override
    public void init() {
        mecanumDrive = new MecanumDrive(this);
        arm = new Arm(this);

        hangRopeMotor = hardwareMap.get(DcMotor.class, "hangMotor");
        hangArmServo = hardwareMap.get(Servo.class, "hangServo");

        telemetry.addData("Hardware: ", "Initialized");
    }

    @Override
    public void loop() {
        mecanumDrive.update();
        arm.update();

        if (gamepad1.dpad_up) {
            hangRopeMotor.setPower(1);
        } else if (gamepad1.dpad_down) {
            hangRopeMotor.setPower(-1);
        } else {
            hangRopeMotor.setPower(0);
        }

        if (gamepad1.a) {
            hangArmServo.setPosition(1); // Temp Close
            telemetry.addData(v4);
        }
        else if (gamepad1.b) {
            hangArmServo.setPosition(0); // Temp Open
        }
    }
}