package org.firstinspires.ftc.teamcode.OpModes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.parts.*;

@TeleOp
public class ResetArm extends OpMode {
    Arm2 arm;


    @Override
    public void init() {
        arm = new Arm2(this);

        telemetry.addData("Hardware: ", "Initialized");
    }

    @Override
    public void start() {
        arm.reset();
    }

    @Override
    public void loop() {

    }
}