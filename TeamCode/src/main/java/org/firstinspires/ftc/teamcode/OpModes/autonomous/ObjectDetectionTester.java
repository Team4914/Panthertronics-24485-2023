package org.firstinspires.ftc.teamcode.OpModes.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

// shared
import org.firstinspires.ftc.teamcode.parts.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Autonomous
public class MainRedBackdrop extends LinearOpMode {
    final static double ROBOT_WIDTH = 16.75;
    final static double ROBOT_LENGTH = 17;
    final static double ROBOT_CENTER_X = ROBOT_WIDTH/2;
    final static double ROBOT_CENTER_Y = ROBOT_LENGTH/2;
    
    final static Vector2d CAMERA_OFFSET = new Vector2d(5,10);
    
    public static Pose2d getCameraPose(Pose2d robotPose) {
        return new Pose2d(
            robotPose.vec().plus(
                CAMERA_OFFSET.rotated(robotPose.getHeading())
            ), 
            robotPose.getHeading()
        );
    }
    
    @Override
    public void runOpMode() {
        // Initialization
        ObjectDetector objD = new ObjectDetector(this);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(ROBOT_WIDTH/2,-72 + ROBOT_LENGTH/2, Math.PI/2);
        drive.setPoseEstimate(startPose);

        /*
        Trajectory path = drive.trajectoryBuilder(startPose)
                .splineTo(new Vector2d(24, -24), 0)
                .build();
        */

        waitForStart();
        if(isStopRequested()) return;

        List<Recognition> currentRecognitions = objD.getRecognitions();
        addTelemetry("# Objects Detected", currentRecognitions.size());

        Collections.sort(currentRecognitions, Comparator.comparingDouble(Recognition::getConfidence));

        // if (recognition.getLabel() == team prop) TODO: check if right type

        if (currentRecognitions.size() != 0) {
            Recognition recognition = currentRecognitions.get(currentRecognitions.size() - 1);
            
            telemetry.addData("Calculated Recognition Position", ObjectDetector.recognitionToFieldPos(
                recognition,
                600,
                2.25,
                getCameraPose(startingPose)
            ));
            telemetry.update();
        }

        objD.close();

        //drive.followTrajectory(path);
    }
}
