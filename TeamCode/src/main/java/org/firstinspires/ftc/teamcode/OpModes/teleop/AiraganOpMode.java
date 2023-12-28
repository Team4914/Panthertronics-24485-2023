package org.firstinspires.ftc.teamcode.OpModes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.parts.*;

@TeleOp
public class AiraganOpMode extends OpMode {
    MecanumDrive mecanumDrive;
    Arm2 arm;
    DcMotor motor;
    Hanger hanger;
    //Roller roller;
    Launcher launcher;


    @Override
    public void init() {
        mecanumDrive = new MecanumDrive(this);
        arm = new Arm2(this);
        //roller = new Roller(this);
        hanger = new Hanger(this);




        //launcher = new Launcher(this);

        //motor = hardwareMap.get(DcMotor.class, "hangMotor");

        telemetry.addData("Hardware: ", "Initialized");
    }

    @Override
    public void start() {
        arm.setState(Arm2.State.BOARD);
    }

    @Override
    public void loop() {
        mecanumDrive.update();
        hanger.update();
        //launcher.update();
        arm.update();
        arm.adjust();
    }
}