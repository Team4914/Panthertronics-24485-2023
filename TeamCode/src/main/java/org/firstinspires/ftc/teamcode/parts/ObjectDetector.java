package org.firstinspires.ftc.teamcode.parts;

import android.util.Size;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ObjectDetector {
    OpMode opMode;

    public static final double CONFIDENCE_REQUIRED = 0.7;
    public static final int TAPE_BORDER = 300;

    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera

    // TFOD_MODEL_ASSET points to a model file stored in the project Asset location,
    // this is only used for Android Studio when using models in Assets.
    private static final String TFOD_MODEL_ASSET = "cube1.tflite";
    // TFOD_MODEL_FILE points to a model file stored onboard the Robot Controller's storage
    // Define the labels recognized in the model for TFOD (must be in training order!)
    private static final String[] LABELS = {
            "Red",
    };;

    public static final Size SCREEN_RESOLUTION = new Size(640, 480);

    /**
     * The variable to store our instance of the TensorFlow Object Detection processor.
     */
    private TfodProcessor tfod;

    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;

    public ObjectDetector(OpMode opMode) {
        this.opMode = opMode;

        initTfod();

        opMode.telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
        opMode.telemetry.addData(">", "Touch Play to start OpMode");
        opMode.telemetry.update();
    }
    public void update() {

        // Save CPU resources; can resume streaming when needed.
        //if (gamepad1.dpad_down) {
        //    visionPortal.stopStreaming();
//                } else if (gamepad1.dpad_up) {
//                    visionPortal.resumeStreaming();
//                }

        // Save more CPU resources when camera is no longer needed.
        //visionPortal.close();

    }   // end runOpMode()

    public List<Recognition> getRecognitions() {
        return tfod.getRecognitions();
    }

    public Recognition getBestRecognition(List<Recognition> list) {
        double maxConf = 0;
        Recognition best = null;

        for (Recognition rec : list) {
            if (rec.getConfidence() > maxConf) {
                best = rec;
                maxConf = rec.getConfidence();
            }
        }

        return best;
    }

    /**
     * Initialize the TensorFlow Object Detection processor.
     */
    private void initTfod() {

        // Create the TensorFlow processor by using a builder.
        tfod = new TfodProcessor.Builder()

                // With the following lines commented out, the default TfodProcessor Builder
                // will load the default model for the season. To define a custom model to load,
                // choose one of the following:
                //   Use setModelAssetName() if the custom TF Model is built in as an asset (AS only).
                //   Use setModelFileName() if you have downloaded a custom team model to the Robot Controller.
                .setModelAssetName(TFOD_MODEL_ASSET)

                // The following default settings are available to un-comment and edit as needed to
                // set parameters for custom models.
                //.setModelLabels(LABELS)
                //.setIsModelTensorFlow2(true)
                //.setIsModelQuantized(true)
                //.setModelInputSize(300)
                //.setModelAspectRatio(16.0 / 9.0)

                .build();

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(opMode.hardwareMap.get(WebcamName.class, "Zain"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        // Choose a camera resolution. Not all cameras support all resolutions.
        builder.setCameraResolution(SCREEN_RESOLUTION);

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        builder.enableLiveView(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(tfod);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Set confidence threshold for TFOD recognitions, at any time.
        tfod.setMinResultConfidence(0.75f);

        // Disable or re-enable the TFOD processor at any time.
        //visionPortal.setProcessorEnabled(tfod, true);

    }   // end method initTfod()

    /**
     * Add telemetry about TensorFlow Object Detection (TFOD) recognitions.
     */

    private void telemetryTfod() {
        List<Recognition> currentRecognitions = getRecognitions();
        opMode.telemetry.addData("# Objects Detected", currentRecognitions.size());

        // Step through the list of recognitions and display info for each one.
        for (Recognition recognition : currentRecognitions) {
            double x = (recognition.getLeft() + recognition.getRight()) / 2 ;
            double y = (recognition.getTop()  + recognition.getBottom()) / 2 ;
            decidePosition(x,y, recognition.getConfidence());
            opMode.telemetry.addData(""," ");
            opMode.telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
            opMode.telemetry.addData("- Position", "%.0f / %.0f", x, y);
            opMode.telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
        }   // end for() loop

        // Push telemetry to the Driver Station.
        opMode.telemetry.update();

    }   // end method telemetryTfod()

    /* requirements: bot is placed on the starting tile, touching the wall,
            with the right wheels touching the right edge of the tile;
            camera is mounted on the bottom right side of the bot

        returns: 0 if team prop is on the left marker
            1 for middle
            2 for right
     */
    public static int decidePosition(double x, double y, double confidence){
        if (confidence < CONFIDENCE_REQUIRED) return 0; // left

        if(x > TAPE_BORDER)
            return 2; // right
        else
            return 1; // center
    }

    public int recognizeTeamPropPosition() {
        List<Recognition> recognitions = getRecognitions();
        if (recognitions.size() == 0) return 0; // default is left

        // if (recognition.getLabel() == team prop) TODO: filter other labels

        Recognition best = getBestRecognition(recognitions);
        double x = (best.getLeft() + best.getRight()) / 2;
        double y = (best.getTop() + best.getBottom()) / 2;

        return ObjectDetector.decidePosition(x, y, best.getConfidence());
    }

    public void close() {
        visionPortal.close();
    }

    // https://support.logi.com/hc/en-ca/articles/360023462093-Logitech-HD-Webcam-C270-Technical-Specifications
    // 4.5 inches in real life at depth 4.5 inches = 1280 px
    static final double SCREEN_DEPTH = 1280; // "focal length" but in px
    
    /*
        All of these methods assume camera is parallel to the ground, camera height is close to that of the object, and the object is not rotated
    */
    
    public static double imageWidthToDepth(double imagePixels, double realInches) {
        return SCREEN_DEPTH / imagePixels * realInches;
    }
    
    // screenX: center of picture = 0; increasing going right
    public static Vector2d imageXToFieldPos(double imageX, double fieldDepth, Pose2d cameraPose) {
        double perpOffset = fieldDepth / SCREEN_DEPTH * imageX;
        
        Vector2d offset = new Vector2d(perpOffset, fieldDepth).rotated(cameraPose.getHeading());
        return cameraPose.vec().plus(offset);
    }
    
    public static Vector2d recognitionToFieldPos(Recognition recognition, double realInches, Pose2d cameraPose) {
        double width = recognition.getImageWidth();
        double depth = imageWidthToDepth(width, realInches);
        
        double imgX = (recognition.getLeft() + recognition.getRight())/2 - SCREEN_RESOLUTION.getWidth()/2d;
        return imageXToFieldPos(imgX, depth, cameraPose);
    }
}
