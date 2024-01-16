package org.firstinspires.ftc.teamcode.OpModes.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

// shared
import org.firstinspires.ftc.teamcode.parts.*;
import org.firstinspires.ftc.teamcode.constants.*;

@TeleOp
public class CVTest extends LinearOpMode {

    public void runOpMode() {
        // Initialization
        ObjectDetector obj = new ObjectDetector(this, "BlueProp.tflite");

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
    }
}
