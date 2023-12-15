package org.firstinspires.ftc.teamcode.OpModes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.parts.*;

@TeleOp
public class AiraganOpMode extends OpMode {
    MecanumDrive mecanumDrive;
    Arm arm;
    DcMotor motor;
    Hanger hanger;
    //Roller roller;
    Launcher launcher;


    @Override
    public void init() {
        mecanumDrive = new MecanumDrive(this);
        arm = new Arm(this);
        //roller = new Roller(this);
        hanger = new Hanger(this);




        //+ = new Launcher(this);

        //motor = hardwareMap.get(DcMotor.class, "hangMotor");

        telemetry.addData("Hardware: ", "Initialized");
    }

    @Override
    public void loop() {
        mecanumDrive.update();
        hanger.update();
        //roller.update();
        //launcher.update();
        arm.update();
    }
}