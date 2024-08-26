package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.roadrunner.DriveShim;

public class WorkshopMain extends WorkshopFunctions {
    public WorkshopMain(DriveShim drive, Pose2d startPose) { super(drive, startPose); }


    public void main() {

        repeat(3, () -> {
            // code goes here
            forward(24);
            turnLeft(90);
        });

    }
}
