package org.firstinspires.ftc.teamcode.OpModes.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

// shared
import org.firstinspires.ftc.teamcode.parts.*;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Autonomous
public class BlueStationCV extends LinearOpMode {
    final static double ROBOT_WIDTH = 16.75;
    final static double ROBOT_LENGTH = 17;
    final static double ROBOT_CENTER_X = ROBOT_WIDTH/2;
    final static double ROBOT_CENTER_Y = ROBOT_LENGTH/2;
    final static double ARM_GROUND_LENGTH = 12; // placeholder value
    final static double CLAW_LENGTH = 4;

    @Override
    public void runOpMode() {
        // Initialization
        ObjectDetector objD = new ObjectDetector(this, "BlueProp.tflite");
        Arm2 arm = new Arm2(this);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(-48 + ROBOT_WIDTH/2,72 - ROBOT_LENGTH/2, -Math.PI/2);
        Pose2d parkPose = new Pose2d(-48 + ROBOT_WIDTH/2,60 - ROBOT_LENGTH/2, -Math.PI/2);
        drive.setPoseEstimate(startPose);

        TrajectorySequence leftTapeTraj = drive.trajectorySequenceBuilder(startPose)
                .splineToLinearHeading(
                        new Pose2d(
                                ROBOT_LENGTH/2 + ARM_GROUND_LENGTH - CLAW_LENGTH/2 -64,
                                24  + ROBOT_LENGTH/2 + 2,
                                0),
                        0
                )
                .addTemporalMarker(arm::openClawLeft)
                .waitSeconds(1)
                .addTemporalMarker(() -> arm.setState(Arm2.State.BOARD))
                //.waitSeconds(1)
                //.splineToLinearHeading(parkPose, 0)
                .build();


        TrajectorySequence middleTapeTraj = drive.trajectorySequenceBuilder(startPose)
                .splineToLinearHeading(new Pose2d(-48 + 12, 24 +    3+ 0.5 + ARM_GROUND_LENGTH - CLAW_LENGTH/2 + ROBOT_LENGTH/2, -Math.PI/2), Math.PI/2)
                .addTemporalMarker(arm::openClawLeft)
                .waitSeconds(1)
                .addTemporalMarker(() -> arm.setState(Arm2.State.BOARD))
                //.waitSeconds(1)
                //.splineToLinearHeading(parkPose, -Math.PI/2)
                .build();

        TrajectorySequence rightTapeTraj = drive.trajectorySequenceBuilder(startPose)
                .splineToLinearHeading(new Pose2d(-48 - 4, +24 + 9.5 + ARM_GROUND_LENGTH - CLAW_LENGTH/2 + ROBOT_LENGTH/2, -Math.PI/2), Math.PI/2)
                .addTemporalMarker(arm::openClawLeft)
                .waitSeconds(1)
                .addTemporalMarker(() -> arm.setState(Arm2.State.BOARD))
                //.waitSeconds(1)
                //.splineToLinearHeading(parkPose, -Math.PI/2)
                .build();

        waitForStart();
        if(isStopRequested()) return;

        arm.closeClawLeft();
        arm.closeClawRight();

        sleep(3000);

        int teamPropPos = objD.recognizeTeamPropPosition();
        objD.close();

        telemetry.addData("Team Prop Pos", teamPropPos);
        telemetry.update();

        arm.setState(Arm2.State.GROUND);

        while (arm.elbowMotorLeft.isBusy() && opModeIsActive())
            sleep(10);
        sleep(2000);

        switch (teamPropPos) {
            case 0:
                drive.followTrajectorySequence(leftTapeTraj);
                break;
            case 1:
                drive.followTrajectorySequence(middleTapeTraj);
                break;
            case 2:
                drive.followTrajectorySequence(rightTapeTraj);
                break;
        }

        // reset arm
        arm.setState(Arm2.State.BOARD);
        while (arm.elbowMotorLeft.isBusy() && opModeIsActive())
            sleep(10);
        sleep(2000);
        arm.setState(Arm2.State.STORAGE);

        while (arm.elbowMotorLeft.isBusy() && opModeIsActive())
            sleep(10);
        sleep(2000);
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

