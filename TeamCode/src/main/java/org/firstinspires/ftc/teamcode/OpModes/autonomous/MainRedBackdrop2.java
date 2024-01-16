package org.firstinspires.ftc.teamcode.OpModes.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

// shared
import org.firstinspires.ftc.teamcode.parts.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Autonomous
public class MainRedBackdrop2 extends OpMode {
    final static double ROBOT_WIDTH = 16.75;
    final static double ROBOT_LENGTH = 17;
    final static double ROBOT_CENTER_X = ROBOT_WIDTH/2;
    final static double ROBOT_CENTER_Y = ROBOT_LENGTH/2;

    SampleMecanumDrive drive;
    ObjectDetector objD;
    boolean isDone = false;
    @Override
    public void init() {
        // Initialization
        telemetry.addData("Testing", 0);
        objD = new ObjectDetector(this, "RedProp.tflite");
        telemetry.addData("Testing", 0.5);

        drive = new SampleMecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(ROBOT_WIDTH/2,-72 + ROBOT_LENGTH/2, Math.PI/2);
        drive.setPoseEstimate(startPose);

        Trajectory path = drive.trajectoryBuilder(startPose)
                .splineTo(new Vector2d(24, -24), 0)
                .build();
    }
    @Override
    public void start() {
        telemetry.addData("Testing", 1);
    }

    @Override
    public void loop() {
        while (!isDone) {
            List<Recognition> currentRecognitions = objD.getRecognitions();
            telemetry.addData("# Objects Detected", currentRecognitions.size());

            Collections.sort(currentRecognitions, Comparator.comparingDouble(Recognition::getConfidence));

            // if (recognition.getLabel() == team prop) TODO: check if right type

            int pos = 0; // default; undetected=left
            telemetry.addData("Testing", 2);
            if (currentRecognitions.size() != 0) {
                Recognition recognition = currentRecognitions.get(currentRecognitions.size() - 1);

                double x = (recognition.getLeft() + recognition.getRight()) / 2;
                double y = (recognition.getTop() + recognition.getBottom()) / 2;

                pos = ObjectDetector.decidePosition(x, y, recognition.getConfidence());
            }
            telemetry.addData("Testing", 3);
            telemetry.addData("Detected Team Prop Position: ", pos);


            objD.close();

            //drive.followTrajectory(path);
            isDone = true;
        }
    }
}

