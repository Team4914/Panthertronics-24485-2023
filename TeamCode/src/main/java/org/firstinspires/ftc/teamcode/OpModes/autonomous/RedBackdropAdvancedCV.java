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
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Autonomous
public class RedBackdropAdvancedCV extends OpMode {
    // Constants
    final static double ROBOT_WIDTH = 16.75;
    final static double ROBOT_LENGTH = 17;
    final static double ARM_GROUND_LENGTH = 8; // placeholder value

    final static int DETECT_INTERVAL = 20;

    final static String TEAM_PROP_LABEL = "Red";
    final static double TEAM_PROP_WIDTH = 2.8;
    final static Pose2d CAMERA_POSE_OFFSET = new Pose2d(8, -8, 0);

    final static double OUTLIER_THRESHOLD = 24;

    final static Vector2d[] TAPE_POSITIONS = {
        new Vector2d(0, -24 - 6), // index 0 = left
        new Vector2d(12, -24), // index 1 = middle
        new Vector2d(24, -24 - 6) // index 2 = right
    };

    // Parts
    ObjectDetector objD;
    Arm2 arm;
    SampleMecanumDrive drive;

    // Trajectories
    Trajectory cvTraj;
    TrajectorySequence leftTapeTraj;
    TrajectorySequence middleTapeTraj;
    TrajectorySequence rightTapeTraj;

    // status attributes
    int detectClock = 0;

    enum State {
        DETECTING_TEAM_PROP,
        PLACING_PRELOADED_PIXELS,
        PARKING,
        IDLING
    }

    State state = State.DETECTING_TEAM_PROP;

    List<Double> recognitionXs = new ArrayList<>();
    List<Double> recognitionYs = new ArrayList<>();
    List<Double> recognitionConfidences = new ArrayList<>();

    public static double confidenceToWeight(double confidence) {
        double b = 2;
        double x = confidence;

        return Math.pow(x,b) / (Math.pow(x,b) + Math.pow(1-x, b));
    }

    @Override
    public void init() {
        // Initialization
        objD = new ObjectDetector(this, "RedProp.tflite");
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

        leftTapeTraj = drive.trajectorySequenceBuilder(cvTraj.end())
                .splineTo(new Vector2d(ARM_GROUND_LENGTH + ROBOT_LENGTH/2, -24), Math.PI)
                .addTemporalMarker(() -> arm.setState(Arm2.State.GROUND))
                .waitSeconds(3)
                .addTemporalMarker(() -> arm.openClawLeft())
                .waitSeconds(1)
                .addTemporalMarker(() -> arm.setState(Arm2.State.BOARD))
                .waitSeconds(1)
                .splineToLinearHeading(new Pose2d(48, -36 + 6, 0), 0)
                .addTemporalMarker(() -> arm.openClawRight())
                .waitSeconds(1)
                .back(12)
                .splineToLinearHeading(parkPose, 0)
                .build();

        middleTapeTraj = drive.trajectorySequenceBuilder(cvTraj.end())
                .splineTo(new Vector2d(24 + ARM_GROUND_LENGTH + ROBOT_LENGTH/2 - 6, -24), Math.PI)
                .addTemporalMarker(() -> arm.setState(Arm2.State.GROUND))
                .waitSeconds(3)
                .addTemporalMarker(() -> arm.openClawLeft())
                .waitSeconds(1)
                .addTemporalMarker(() -> arm.setState(Arm2.State.BOARD))
                .waitSeconds(1)
                .splineToLinearHeading(new Pose2d(48, -36 + 0, 0), 0)
                .addTemporalMarker(() -> arm.openClawRight())
                .waitSeconds(1)
                .back(12)
                .splineToLinearHeading(parkPose, 0)
                .build();

        leftTapeTraj = drive.trajectorySequenceBuilder(cvTraj.end())
                .splineTo(new Vector2d(24 + ARM_GROUND_LENGTH + ROBOT_LENGTH/2, -24), Math.PI)
                .addTemporalMarker(() -> arm.setState(Arm2.State.GROUND))
                .waitSeconds(3)
                .addTemporalMarker(() -> arm.openClawLeft())
                .waitSeconds(1)
                .addTemporalMarker(() -> arm.setState(Arm2.State.BOARD))
                .waitSeconds(1)
                .splineToLinearHeading(new Pose2d(48, -36 - 6, 0), 0)
                .addTemporalMarker(() -> arm.openClawRight())
                .waitSeconds(1)
                .back(12)
                .splineToLinearHeading(parkPose, 0)
                .build();
    }

    @Override
    public void start() {
        arm.closeClawLeft();
        arm.closeClawRight();

        state = State.DETECTING_TEAM_PROP;
        drive.followTrajectoryAsync(cvTraj);
    }

    @Override
    public void loop() {

        switch (state) {
            case DETECTING_TEAM_PROP:
                if (drive.isBusy()) { // still moving to position and taking pictures
                    detectClock--;

                    if (detectClock < 0) { // try to detect
                        detectClock = DETECT_INTERVAL;

                        Pose2d curPose = drive.getPoseEstimate();
                        Pose2d cameraPose = new Pose2d(
                                curPose.vec().plus(CAMERA_POSE_OFFSET.vec().rotated(curPose.getHeading())),
                                curPose.getHeading() + CAMERA_POSE_OFFSET.getHeading()
                        );

                        List<Recognition> currentRecognitions = objD.getRecognitions();
                        telemetry.addData("# Objects Detected", currentRecognitions.size());

                        for (Recognition recognition : currentRecognitions) {
                            //if (!recognition.getLabel().equals(TEAM_PROP_LABEL)) continue;

                            Vector2d propPos = ObjectDetector.recognitionToFieldPos(recognition, TEAM_PROP_WIDTH, cameraPose);

                            recognitionXs.add(propPos.getX());
                            recognitionYs.add(propPos.getY());
                            recognitionConfidences.add((double) recognition.getConfidence());
                        }
                    }
                } else { // arrived at position; calculate team prop pos and switch state
                    // remove outliers
                    List<Double> sortedXs = recognitionXs.stream().sorted().collect(Collectors.toList());
                    List<Double> sortedYs = recognitionYs.stream().sorted().collect(Collectors.toList());

                    double medianX, medianY;

                    if (sortedXs.size() % 2 == 0)
                        medianX = sortedXs.get(sortedXs.size() / 2 - 1) + sortedXs.get(sortedXs.size() / 2);
                    else medianX = sortedXs.get(sortedXs.size() / 2);

                    if (sortedYs.size() % 2 == 0)
                        medianY = sortedYs.get(sortedYs.size() / 2 - 1) + sortedYs.get(sortedYs.size() / 2);
                    else medianY = sortedYs.get(sortedYs.size() / 2);

                    for (int i = 0; i < recognitionXs.size(); i++) {
                        if (Math.abs(recognitionXs.get(i) - medianX) > OUTLIER_THRESHOLD
                                || Math.abs(recognitionYs.get(i) - medianY) > OUTLIER_THRESHOLD)
                            recognitionConfidences.set(i, 0d);
                    }

                    // calculate position by weighted average
                    double xSum = 0, ySum = 0, weightsSum = 0;

                    for (int i = 0; i < recognitionXs.size(); i++) {
                        double weight = confidenceToWeight(recognitionConfidences.get(i));

                        weightsSum += weight;
                        xSum += weight * recognitionXs.get(i);
                        ySum += weight * recognitionYs.get(i);
                    }

                    int closestTape = 0;

                    if (weightsSum != 0) {
                        Vector2d avgPos = new Vector2d(xSum / weightsSum, ySum / weightsSum);

                        // find closest tape pos (team props are placed at the midpoint of the tape)
                        double closestDist = Double.MAX_VALUE;

                        for (int i = 0; i < TAPE_POSITIONS.length; i++) {
                            if (avgPos.distTo(TAPE_POSITIONS[i]) < closestDist)
                                closestTape = i;
                        }
                    }

                    // follow corresponding trajectory

                    switch (closestTape) {
                        case 0: // left tape
                            drive.followTrajectorySequenceAsync(leftTapeTraj);
                            break;
                        case 1: // left tape
                            drive.followTrajectorySequenceAsync(middleTapeTraj);
                            break;
                        case 2: // left tape
                            drive.followTrajectorySequenceAsync(rightTapeTraj);
                            break;
                    }

                    state = State.PLACING_PRELOADED_PIXELS;
                }

                break;

            case PLACING_PRELOADED_PIXELS:
                if (!drive.isBusy()) {
                    state = State.IDLING;
                    arm.setState(Arm2.State.STORAGE);
                }

                break;

            case IDLING: break;
        }

        drive.update();
    }
}

