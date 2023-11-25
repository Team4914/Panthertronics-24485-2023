package org.firstinspires.ftc.teamcode.Airagan.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


// Shared Import
import org.firstinspires.ftc.teamcode.Hamza.OpModes.Launcher;
import org.firstinspires.ftc.teamcode.shared.*;

@TeleOp
public class AiraganOpMode extends OpMode {
    MecanumDrive mecanumDrive;
    //Arm arm;
    DcMotor motor;

    Hanger hanger;
    Launcher launcher;

    @Override
    public void init() {
        mecanumDrive = new MecanumDrive(this);
        //arm = new Arm(this);

        hanger = new Hanger(this);














        + = new Launcher(this);

        motor = hardwareMap.get(DcMotor.class, "hangMotor");

        telemetry.addData("Hardware: ", "Initialized");
    }

    @Override
    public void loop() {
        mecanumDrive.update();
        hanger.update();
        launcher.update();

        //arm.update();
    }
}