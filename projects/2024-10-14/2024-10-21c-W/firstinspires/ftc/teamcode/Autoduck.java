package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;



@Autonomous(name="AutoDuck", group="Autonomous")
public class Autoduck extends LinearOpMode {

    // todo: write your code here
    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;
    private Servo servo0;
    
    public void runOpMode(){
        // Initialize motors
        frontLeftMotor = hardwareMap.get(DcMotor.class, "backLeft");
        frontRightMotor = hardwareMap.get(DcMotor.class, "backRight");
        backLeftMotor = hardwareMap.get(DcMotor.class, "frontLeft");
        backRightMotor = hardwareMap.get(DcMotor.class, "frontRight");
        servo0 = hardwareMap.get(Servo.class, "servo0");
    
        // Reverse motor directions as needed
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart();
        servo0.setPosition(0.0);
        sleep(5000);
        servo0.setPosition(1.0);
        sleep(1000);
        telemetry.addData("INFO", "DONE");
        telemetry.update();
    }
/*
    void motorDrive(double power){
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
    }
    void drive(double rotation, double dist, double speed){
        frontLeft.setPower();
        motorDrive(speed);
    }
    void rotate(int direction){
        if (direction == 5){
            frontLeft.setPower(0.7);
            backRight.setPower(0.7);
            frontRight.setPower(-0.7);
            backLeft.setPower(-0.7);
        } else {
            telemetry.addData("INFO", "NO WORKY");
        }
    }
*/
}







