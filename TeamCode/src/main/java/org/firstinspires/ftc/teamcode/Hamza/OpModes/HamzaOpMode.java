package org.firstinspires.ftc.teamcode.Hamza.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

// Shared Import
import org.firstinspires.ftc.teamcode.shared.*;
@TeleOp
public class HamzaOpMode extends OpMode {
    MecanumDrive mecanumDrive;
    Arm arm;

    DcMotor hangRopeMotor;
    DcMotor rollerMotor;
    Servo hangArmServo;


    @Override
    public void init() {
        mecanumDrive = new MecanumDrive(this);
        arm = new Arm(this);

        //hangRopeMotor = hardwareMap.get(DcMotor.class, "hangMotor");
        //hangArmServo = hardwareMap.get(Servo.class, "hangServo");

        rollerMotor = hardwareMap.get(DcMotor.class, "a");

        telemetry.addData("Hardware: ", "Initialized");
    }

    @Override
    public void loop() {
        mecanumDrive.update();
        arm.update();
        /*
        if (gamepad1.dpad_up) {
            hangRopeMotor.setPower(1);
        } else if (gamepad1.dpad_down) {
            hangRopeMotor.setPower(-1);
        } else {
            hangRopeMotor.setPower(0);
        }

        if (gamepad1.a) {
            hangArmServo.setPosition(1); // Temp Close;
        }
        else if (gamepad1.b) {
            hangArmServo.setPosition(0); // Temp Open
        }
        */
        if (gamepad1.a) {
            rollerMotor.setPower(1);
        }
    }
}