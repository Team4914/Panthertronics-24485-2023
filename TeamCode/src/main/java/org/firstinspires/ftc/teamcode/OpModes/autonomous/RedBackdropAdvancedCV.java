package org.firstinspires.ftc.teamcode.OpModes.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;

// shared
import org.firstinspires.ftc.teamcode.parts.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Autonomous
public class RedBackdropAdvancedCV extends OpMode {
    // Constants
    final static double ROBOT_WIDTH = 16.75;
    final static double ROBOT_LENGTH = 17;
    final static double ARM_GROUND_LENGTH = 8; // placeholder value

    final static double

    // Parts
    ObjectDetector objD;
    Arm2 arm;
    SampleMecanumDrive drive;

    // Trajectories
    Trajectory cvTraj;
    Trajectory leftTapeTraj;
    Trajectory middleTapeTraj;
    Trajectory rightTapeTraj;

    // status attributes


    @Override
    public void init() {
        // Initialization
        objD = new ObjectDetector(this);
        arm = new Arm2(this);

        drive = new SampleMecanumDrive(hardwareMap);

        // starts touching right line
        Pose2d startPose = new Pose2d(24 - ROBOT_WIDTH / 2, -72 + ROBOT_LENGTH / 2, Math.PI / 2);
        drive.setPoseEstimate(startPose);

        cvTraj = drive.trajectoryBuilder(startPose)
                .splineToLinearHeading(
                        new Pose2d(48, -24, Math.PI), Math.PI/2,
                        // slow down the spline by reducing max velocity
                        SampleMecanumDrive.getVelocityConstraint(15, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL)
                )
                .build();

        Pose2d parkPose = new Pose2d(60, -60, Math.PI);

        leftTapeTraj = drive.trajectoryBuilder(cvTraj.end())
                .splineTo(new Vector2d(ARM_GROUND_LENGTH + ROBOT_LENGTH/2, -24), Math.PI)
                .back(12)
                .splineToLinearHeading(new Pose2d(48, -36 + 6, 0), 0)
                .back(12)
                .splineToLinearHeading(parkPose, 0)
                .build();

        middleTapeTraj = drive.trajectoryBuilder(cvTraj.end())
                .splineTo(new Vector2d(24 + ARM_GROUND_LENGTH + ROBOT_LENGTH/2 - 6, -24), Math.PI)
                .back(12)
                .splineToLinearHeading(new Pose2d(48, -36 + 0, 0), 0)
                .back(12)
                .splineToLinearHeading(parkPose, 0)
                .build();

        leftTapeTraj = drive.trajectoryBuilder(cvTraj.end())
                .splineTo(new Vector2d(24 + ARM_GROUND_LENGTH + ROBOT_LENGTH/2, -24), Math.PI)
                .back(12)
                .splineToLinearHeading(new Pose2d(48, -36 - 6, 0), 0)
                .back(12)
                .splineToLinearHeading(parkPose, 0)
                .build();
    }

    @Override
    public void start() {
        arm.closeClawLeft();
        arm.closeClawRight();
    }

    @Override
    public void loop() {
        drive.update();

        Pose2d curPos = drive.getPoseEstimate();

        /*
        List<Recognition> currentRecognitions = objD.getRecognitions();
        addTelemetry("# Objects Detected", currentRecognitions.size());

        Collections.sort(currentRecognitions, Comparator.comparingDouble(Recognition::getConfidence));

        // if (recognition.getLabel() == team prop) TODO: check if right type

        int pos = 0; // default; undetected=left

        if (currentRecognitions.size() != 0) {
            Recognition recognition = currentRecognitions.get(currentRecognitions.size() - 1);

            double x = (recognition.getLeft() + recognition.getRight()) / 2;
            double y = (recognition.getTop() + recognition.getBottom()) / 2;

            pos = ObjectDetector.decidePosition(x, y, recognition.getConfidence());
        }
        addTelemetry("Detected Team Prop Position: ", pos);

        objD.close();
        */



//        drive.followTrajectory(path1);
//
//        // drop off pixel
//        arm.setState(Arm2.State.BOARD);
//
//        while (arm.elbowMotorLeft.isBusy() && opModeIsActive())
//            sleep(10);
//
//        arm.openClawLeft();
//        arm.openClawRight();
//
//        sleep(1000);
//
//        drive.followTrajectory(path2);
//
//        // reset arm
//        arm.setState(Arm2.State.STORAGE);
    }
}

