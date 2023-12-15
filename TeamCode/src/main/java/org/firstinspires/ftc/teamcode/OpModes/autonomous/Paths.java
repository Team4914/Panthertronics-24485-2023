package org.firstinspires.ftc.teamcode.OpModes.autonomous;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.*;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;


public class Paths {
    static SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
    final static double ROBOT_WIDTH = 16.75;
    final static double ROBOT_LENGTH = 17;
    final static double ROBOT_CENTER_X = ROBOT_WIDTH/2;
    final static double ROBOT_CENTER_Y = ROBOT_LENGTH/2;

    // Poses
    public static Pose2d startPose = new Pose2d(ROBOT_WIDTH/2,-72 + ROBOT_LENGTH/2, Math.PI/2);

    public static Trajectory redBoardStart = drive.trajectoryBuilder(startPose)
            .splineTo(new Vector2d(24, -18), 0)
            .build();

//    public static void initialize() {
//        Trajectory redBoardStart = drive.trajectoryBuilder(startPose)
//                .splineTo(new Vector2d(24, -24), 0)
//                .build();
//    }
}
