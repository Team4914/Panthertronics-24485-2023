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
public class MainBlueBackdrop extends LinearOpMode {
    final static double ROBOT_WIDTH = 16.75;
    final static double ROBOT_LENGTH = 17;
    final static double ROBOT_CENTER_X = ROBOT_WIDTH/2;
    final static double ROBOT_CENTER_Y = ROBOT_LENGTH/2;
    @Override
    public void runOpMode() {
        // Initialization
        ObjectDetector objD = new ObjectDetector(this);
        Arm2 arm = new Arm2(this);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Pose2d startPose = new Pose2d(24 - ROBOT_WIDTH/2,72 - ROBOT_LENGTH/2, Math.PI/2);
        drive.setPoseEstimate(startPose);

        Trajectory path1 = drive.trajectoryBuilder(startPose)
                .splineTo(new Vector2d(48, 36), 0)
                .build();

        Trajectory path2 = drive.trajectoryBuilder(path1.end())
                .back(6)
                .splineTo(new Vector2d(60, 60), 0)
                        .build();

        waitForStart();
        if(isStopRequested()) return;

        arm.closeClawLeft();
        arm.closeClawRight();

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
        drive.followTrajectory(path1);

        // drop off pixel
        arm.setState(Arm2.BOARD_STATE);

        while (arm.elbowMotorLeft.isBusy() && opModeIsActive())
            sleep(10);

        arm.openClawLeft();
        arm.openClawRight();

        sleep(1000);

        drive.followTrajectory(path2);

        // reset arm
        arm.setState(Arm2.STORAGE_STATE);
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
