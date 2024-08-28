package org.firstinspires.ftc.teamcode.OpModes.workshop;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

public class WorkshopMain extends WorkshopFunctions {
    public WorkshopMain(SampleMecanumDrive drive, Pose2d startPose) { super(drive, startPose); }


    public void main() {

//        repeat(3, () -> {
//            // code goes here
//            forward(24);
//            turnLeft(90);
//        });
        turnLeft(180);
        forward(3*24);
        turnLeft(-90);
        forward(2*24);
        turnLeft(-90);
        forward(2*24);
    }
}
