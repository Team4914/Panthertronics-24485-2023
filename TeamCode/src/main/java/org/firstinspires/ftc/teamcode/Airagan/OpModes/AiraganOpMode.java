package org.firstinspires.ftc.teamcode.Airagan.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

// Shared Import
import org.firstinspires.ftc.teamcode.shared.*;

@TeleOp
public class AiraganOpMode extends OpMode {
    MecanumDrive mecanumDrive;
    //Arm arm;

    @Override
    public void init() {
        mecanumDrive = new MecanumDrive(this);
        //arm = new Arm(this);

        telemetry.addData("Hardware: ", "Initialized");
    }

    @Override
    public void loop() {
        mecanumDrive.update();
        //arm.update();
    }
}