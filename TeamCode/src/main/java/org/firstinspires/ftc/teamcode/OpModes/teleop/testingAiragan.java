package org.firstinspires.ftc.teamcode.OpModes.teleop;
 
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
 
 
@TeleOp
public class testingAiragan extends OpMode {
    DcMotor motor, motor1;
    Servo servo1;
    Servo servo;
    DcMotor RFMotor;
    DcMotor LFMotor;
    DcMotor RBMotor;
 
    DcMotor LBMotor;
    public void moveDriveTrain() {
        double vertical = 0; // Front-back motion
        double horizontal = 0; // Left-right motion
        double pivot = 0; // Turn angle
 
        /*Depending on what motors are flipped, vertical must be negative or positive.
        * Test with robot, and change to positive if necessary */
        vertical = gamepad1.left_stick_y;
        horizontal = gamepad1.left_stick_x;
        pivot = gamepad1.right_stick_x;
 
        // Diagonally Alternate Motors must be the same
        /* If robot does not work properly, set the vertical values for RB and LF to positive*/
        RFMotor.setPower(-pivot + (vertical - horizontal));
        RBMotor.setPower(-pivot + (vertical + horizontal));
        LFMotor.setPower(pivot + (vertical + horizontal));
        LBMotor.setPower(pivot + (vertical - horizontal));
    }
    @Override
    public void init() {
        motor = hardwareMap.get(DcMotor.class, "motor");
        //motor1 = hardwareMap.get(DcMotor.class, "motor1");
 
        //motor1.setDirection(DcMotorSimple.Direction.REVERSE);
 
        //servo = hardwareMap.get(Servo.class, "claw");
        //servo1 = hardwareMap.get(Servo.class, "arm");
        servo = hardwareMap.get(Servo.class, "im_cookin");
        telemetry.addData("Hardware: ", "Initialized");
 
        RFMotor = hardwareMap.get(DcMotor.class, "right_front_drive");
        LFMotor = hardwareMap.get(DcMotor.class, "left_front_drive");
        RBMotor = hardwareMap.get(DcMotor.class, "right_back_drive");
        LBMotor = hardwareMap.get(DcMotor.class, "left_back_drive");
        telemetry.addData("Hardware: ", "Initialized");
 
        RFMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        RBMotor.setDirection(DcMotorSimple.Direction.REVERSE);
      }
 
      public void arm(){
          double pow = 0;
          //motor1.setPower(gamepad1.left_stick_y);
          if(gamepad1.dpad_up) {
              // move up.
              pow = 1;
              //motor.setPower(1);
              telemetry.addData("Key", "dpad_up");
              //servo.setPosition(0);
          } else if (gamepad1.dpad_down) {
              pow = -1;
              //motor.setPower(-1);
              // move to 180 degrees.
              telemetry.addData("Key", "dpad_down");
              //servo.setPosition(1);
          }
 
          motor.setPower(pow);
      }
    @Override
    public void loop() {
        arm();
        moveDriveTrain();
 
        if(gamepad1.a) {
            servo.setPosition(0);
            telemetry.addData("Key", "a");
        } else if(gamepad1.b) {
 
            servo.setPosition(1);
            telemetry.addData("Key", "b");
        }
 
    }
}
