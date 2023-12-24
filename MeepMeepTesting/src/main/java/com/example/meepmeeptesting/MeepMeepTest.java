package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTest {
    final static double ROBOT_WIDTH = 16.75;
    final static double ROBOT_LENGTH = 17;
    final static double ROBOT_CENTER_X = ROBOT_WIDTH/2;
    final static double ROBOT_CENTER_Y = ROBOT_LENGTH/2;

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(24 - ROBOT_WIDTH/2,-72 + ROBOT_LENGTH/2, Math.PI/2))
                                .splineTo(new Vector2d(48, -36), 0)
                                .addDisplacementMarker(() -> {
                                    /* Everything in the marker callback should be commented out */
                                    // Arm shenanigans
                                })
                                .back(6)
                                .splineTo(new Vector2d(60, -60), 0)
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}