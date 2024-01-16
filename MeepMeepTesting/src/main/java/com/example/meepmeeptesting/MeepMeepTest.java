package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTest {
    final static double ROBOT_WIDTH = 16.75;
    final static double ROBOT_LENGTH = 17;
    final static double ROBOT_CENTER_X = ROBOT_WIDTH/2;
    final static double ROBOT_CENTER_Y = ROBOT_LENGTH/2;
    final static double ARM_GROUND_LENGTH = 12; // placeholder value
    final static double CLAW_LENGTH = 4;

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);
        /*

        drive.setPoseEstimate(startPose);

        Trajectory path1 = drive.trajectoryBuilder(startPose)
                .splineTo(new Vector2d(48, -36), 0)
                .build();

        Trajectory path2 = drive.trajectoryBuilder(path1.end())
                .back(6)
                .splineTo(new Vector2d(60, -60), 0)
                .build();
        */

        Pose2d startPose = new Pose2d(0 + ROBOT_WIDTH/2,72 - ROBOT_LENGTH/2, -Math.PI/2);
        Pose2d parkPose = new Pose2d(60, 60, Math.PI);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(startPose)
                                .splineToLinearHeading(new Pose2d(24, 24 + 10.5 + ARM_GROUND_LENGTH - CLAW_LENGTH/2 + ROBOT_LENGTH/2, -Math.PI/2), Math.PI/2)
                                //.addTemporalMarker(arm::openClawLeft)
                                .waitSeconds(1)
                                //.addTemporalMarker(() -> arm.setState(Arm2.State.BOARD))
                                .waitSeconds(1)
                                .back(6)
                                .strafeLeft(8)
                                .forward(14)
                                .turn(Math.PI/2)
                                .splineToLinearHeading(new Pose2d(47, 36 + 9, 0), 0)
                                //.addTemporalMarker(arm::openClawRight)
                                .waitSeconds(1)
                                .back(12)
                                .turn(Math.PI)
                                .splineToLinearHeading(parkPose, 0)
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}