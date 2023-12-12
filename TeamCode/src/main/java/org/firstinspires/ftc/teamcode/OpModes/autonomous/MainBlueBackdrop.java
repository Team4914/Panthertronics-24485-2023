package org.firstinspires.ftc.teamcode.OpModes.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous
public class MainBlueBackdrop extends LinearOpMode {
    final static double ROBOT_WIDTH = 16.75;
    final static double ROBOT_LENGTH = 17;
    final static double ROBOT_CENTER_X = ROBOT_WIDTH/2;
    final static double ROBOT_CENTER_Y = ROBOT_LENGTH/2;
    @Override
    public void runOpMode() {
        // Initialization
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(ROBOT_WIDTH/2,72 - ROBOT_LENGTH/2, -Math.PI/2);
        drive.setPoseEstimate(startPose);

        Trajectory path = drive.trajectoryBuilder(startPose)
                .splineTo(new Vector2d(24, 24), 0)
                .build();

        waitForStart();

        if(isStopRequested()) return;

        drive.followTrajectory(path);
    }
}
