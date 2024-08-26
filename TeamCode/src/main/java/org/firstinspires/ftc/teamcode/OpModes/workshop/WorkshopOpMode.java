package org.firstinspires.ftc.teamcode.OpModes.workshop;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

// shared
import org.firstinspires.ftc.teamcode.parts.*;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(group="Workshop")
public class WorkshopOpMode extends LinearOpMode {

    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        WorkshopMain workshopMain = new WorkshopMain(drive);
        TrajectorySequence traj = workshopMain.getTrajectorySequence();

        waitForStart();
        if(isStopRequested()) return;

        drive.followTrajectorySequence(traj);
    }

    // Telemetry
    private void addTelemetry(String key, String val) {
        telemetry.addData(key, val);
        telemetry.update();
    }
    private void addTelemetry(String key, int val) {
        telemetry.addData(key, val);
        telemetry.update();
    }
    private void addTelemetry(String key, double val) {
        telemetry.addData(key, val);
        telemetry.update();
    }
}

