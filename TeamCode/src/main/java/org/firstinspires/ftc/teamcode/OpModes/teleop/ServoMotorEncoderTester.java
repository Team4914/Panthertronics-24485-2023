package org.firstinspires.ftc.teamcode.OpModes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.parts.TappedButton;

@TeleOp
public class ServoMotorEncoderTester extends OpMode {
    public static final double SERVO_SPEED = 0.001;
    public static final double SERVO_FINE_TUNE_SPEED = SERVO_SPEED * 0.1;

    public static final double MOTOR_SPEED = 1;
    public static final double MOTOR_FINE_TUNE_SPEED = SERVO_SPEED * 0.2;

    public static final String CONTROLS = "X to switch modes; Y to enable or disable; A and B to switch ports; \nTriggers to change position/power; Bumpers to fine tune;";

    TappedButton changeModeBut = new TappedButton();
    TappedButton nextPortBut = new TappedButton();
    TappedButton prevPortBut = new TappedButton();
    TappedButton enableBut = new TappedButton();

    String mode = "Servo";
    boolean enabled = false;

    Servo[] servos = new Servo[12];
    int servoNum = 0;
    double servoPos = 0;

    DcMotor[] motors = new DcMotor[8];
    int motorNum = 0;

    public void init() {
        telemetry.addData("Controls: ", CONTROLS);
        telemetry.addData("NOTE", "Currently does not support continuous rotation servos.");

        for (int i = 0; i < servos.length; i++) {
            servos[i] = hardwareMap.get(Servo.class, "s" + Integer.toString(i));
        }

        for (int i = 0; i < motors.length; i++) {
            motors[i] = hardwareMap.get(DcMotor.class, "m" + Integer.toString(i));
            motors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motors[i].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        telemetry.addData("Hardware: ", "Initialized");
    }

    public void loop() {
        telemetry.addData("Controls: ", CONTROLS);
        telemetry.addData("Mode: ", mode);

        resetParts();

        enabled = enableBut.update(gamepad1.y) ^ enabled;

        telemetry.addData("Enabled", enabled);

        if (enabled) {
            if (mode.equals("Servo")) updateServo();
            else if (mode.equals("Motor")) updateMotor();
        }
    }

    void resetParts() {
        for (Servo servo : servos)
            servo.setPosition(0);

        for (DcMotor motor : motors)
            motor.setPower(0);
    }

    void updateServo() {
        if (changeModeBut.update(gamepad1.x)) mode = "Motor";

        if (nextPortBut.update(gamepad1.a)) servoNum--;
        if (prevPortBut.update(gamepad1.b)) servoNum++;

        servoNum = servoNum % 12;

        servoPos -= gamepad1.left_trigger * SERVO_SPEED;
        servoPos += gamepad1.right_trigger * SERVO_SPEED;

        servoPos -= (gamepad1.left_bumper? 1 : 0) * SERVO_FINE_TUNE_SPEED;
        servoPos += (gamepad1.right_bumper? 1 : 0) * SERVO_FINE_TUNE_SPEED;

        servoPos = Math.max(servoPos, 0);
        servoPos = Math.min(servoPos, 1);

        telemetry.addData("Servo Target Position: ", servoPos);

        servos[servoNum].setPosition(servoPos);

        int deviceNum = servoNum / 6;
        if (deviceNum == 0) telemetry.addData("Device: ", "Control Hub");
        else telemetry.addData("Device: ", "Expansion Hub " + deviceNum);

        telemetry.addData("Port: ", servoNum % 6);
    }

    void updateMotor() {
        if (changeModeBut.update(gamepad1.x)) mode = "Servo";

        if (nextPortBut.update(gamepad1.a)) motorNum--;
        if (prevPortBut.update(gamepad1.b)) motorNum++;

        motorNum = motorNum % 8;

        double motorMove = 0;

        motorMove -= gamepad1.left_trigger * MOTOR_SPEED;
        motorMove += gamepad1.right_trigger * MOTOR_SPEED;

        motorMove -= (gamepad1.left_bumper? 1 : 0) * MOTOR_FINE_TUNE_SPEED;
        motorMove += (gamepad1.right_bumper? 1 : 0) * MOTOR_FINE_TUNE_SPEED;

        motorMove = Math.max(motorMove, 0);
        motorMove = Math.min(motorMove, 1);

        telemetry.addData("Motor Power: ", motorMove);

        motors[motorNum].setPower(motorMove);

        telemetry.addData("Motor Encoder Reading: ", motors[motorNum].getCurrentPosition());

        int deviceNum = motorNum / 4;
        if (deviceNum == 0) telemetry.addData("Device: ", "Control Hub");
        else telemetry.addData("Device: ", "Expansion Hub " + deviceNum);

        telemetry.addData("Port: ", motorNum % 4);
    }
}
