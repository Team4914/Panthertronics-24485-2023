package org.firstinspires.ftc.teamcode.OpModes.workshop;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequenceBuilder;

public abstract class WorkshopFunctions {
    TrajectorySequenceBuilder builder;

    public WorkshopFunctions(SampleMecanumDrive drive, Pose2d startPose) {
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