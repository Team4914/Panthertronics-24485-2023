package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequenceBuilder;

public abstract class WorkshopFunctions {
    TrajectorySequenceBuilder builder;

    public WorkshopFunctions(DriveShim drive, Pose2d startPose) {
        drive.setPoseEstimate(startPose);

        builder = drive.trajectorySequenceBuilder(startPose);
    }
    public abstract void main();
    public TrajectorySequence getTrajectorySequence() {
        main();

        return builder.build();
    }

    public void forward(double inches) {
        builder.forward(inches);
    }

    public void turnLeft(double degrees) {
        builder.turn(Math.toRadians(degrees));
    }

    public void repeat(int times, Runnable func) {
        for (int i = 0; i < times; i++) {
            func.run();
        }
    }
}

